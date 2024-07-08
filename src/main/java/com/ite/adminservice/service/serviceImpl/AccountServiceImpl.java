package com.ite.adminservice.service.serviceImpl;

import com.ite.adminservice.config.SystemLogger;
import com.ite.adminservice.constants.LogStepConstant;
import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.constants.RegexConstant;
import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.entities.EStatus;
import com.ite.adminservice.entities.Status;
import com.ite.adminservice.event.eventProducer.AccountEventPublisher;
import com.ite.adminservice.event.eventProducer.NotificationEventPublisher;
import com.ite.adminservice.event.messages.ApproveAccountNotification;
import com.ite.adminservice.event.messages.ResetPasswordMessage;
import com.ite.adminservice.payload.dto.AdminDTO;
import com.ite.adminservice.payload.request.*;
import com.ite.adminservice.repositories.AdminRepository;
import com.ite.adminservice.repositories.StatusRepository;
import com.ite.adminservice.security.CustomAdminDetailsService;
import com.ite.adminservice.security.jwt.JwtUtil;
import com.ite.adminservice.security.tfa.TfaService;
import com.ite.adminservice.service.AccountService;
import com.ite.adminservice.service.RedisService;
import com.ite.adminservice.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TfaService tfaService;

    @Autowired
    private NotificationEventPublisher notificationEventPublisher;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;

    @Autowired
    private SystemLogger logger;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountEventPublisher accountEventPublisher;

    @Override
    public ResponseEntity<?> approveAccount(ApproveAccountRequest request) {
        try {
            logger.log(Thread.currentThread().getName(), "Approve admin account", LogStepConstant.BEGIN_PROCESS,request.getId());
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            Admin admin = adminRepository.findAdminByAdminId(request.getId().trim());
            if(Objects.isNull(admin)){
                return ResponseUtil.getResponseEntity(MessageConstant.ADMIN_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            admin.setStatus(EStatus.ACTIVE);

            logger.log(Thread.currentThread().getName(), "Approve admin account", LogStepConstant.BEGIN_CALL_DATABASE,admin.toString());
            adminRepository.save(admin);
            logger.log(Thread.currentThread().getName(), "Approve admin account", LogStepConstant.END_CALL_DATABASE,"Update status account successful: "+admin.getEmail());

            String qr = tfaService.generateQrCodeImageUri(admin.getSecret());
            ApproveAccountNotification approveAccountNotification = new ApproveAccountNotification(admin.getEmail(),admin.getName(),qr);
            AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
            adminDTO.setAction("APPROVE_ADMIN_ACCOUNT");

            accountEventPublisher.updateAdminAccountEvent(adminDTO);
            notificationEventPublisher.publishAccountApprovalEventWithQRCode(approveAccountNotification);

            logger.log(Thread.currentThread().getName(), "Approve admin account", LogStepConstant.END_PROCESS,"Approved account successful");
            return ResponseUtil.getResponseEntity(MessageConstant.ACCOUNT_APPROVED_SUCCESSFULLY, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.error("An error occurred while approving account for email: {}", request.getId(),e);
            logger.logError(Thread.currentThread().getName(), "Approve admin account",LogStepConstant.END_PROCESS,e.toString());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateStatusAccount(UpdateStatusAccountRequest request) {
       try{
           logger.log(Thread.currentThread().getName(), "Update account status", LogStepConstant.BEGIN_PROCESS,request.toString());

           Admin admin = adminRepository.findAdminByAdminId(request.getAdminId());
           Status status = statusRepository.findStatusByStatusName(request.getStatus());

           if(Objects.isNull(admin) || Objects.isNull(status)){
               return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);
           }
           if(status.getStatusName().equals(EStatus.ACTIVE)){
               admin.setFailedLogin(0);
           }
           admin.setStatus(status.getStatusName());
           adminRepository.save(admin);
           AdminDTO adminDTO = modelMapper.map(admin,AdminDTO.class);

           if(status.getStatusName().equals(EStatus.LOCK)){
               redisService.delete(admin.getAdminId());
           }

           accountEventPublisher.updateAdminAccountEvent(adminDTO);

           logger.log(Thread.currentThread().getName(), "Update account status", LogStepConstant.END_PROCESS,request.toString());
           return ResponseUtil.getResponseEntity(MessageConstant.UPDATE_ACCOUNT_STATUS_SUCCESSFULLY,HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
           logger.log(Thread.currentThread().getName(), "Update account status", LogStepConstant.END_PROCESS,e.toString());
           return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        try{
            logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.BEGIN_PROCESS, "");

            if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getReNewPassword())){
                logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS,MessageConstant.passwordMismatch);
                return ResponseUtil.getResponseEntity("00",MessageConstant.passwordMismatch, HttpStatus.BAD_REQUEST);

            }
            if(!isValidPassword(changePasswordRequest.getNewPassword())){
                logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS, MessageConstant.INVALID_DATA);
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Admin admin = customAdminDetailsService.getAdminDetail();

            if(!encoder.matches(changePasswordRequest.oldPassword, admin.getPassword())){
                logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS, "old password is wrong");
                return ResponseUtil.getResponseEntity("06",MessageConstant.ERROR_06, HttpStatus.BAD_REQUEST);
            }

            admin.setPassword(encoder.encode(changePasswordRequest.newPassword));
            adminRepository.save(admin);
            accountEventPublisher.updateAdminAccountEvent(modelMapper.map(admin,AdminDTO.class));

            logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS, admin.getAdminId());
            return ResponseUtil.getResponseEntity("00",MessageConstant.CHANGED_PASSWORD_SUCCESSFULLY, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.logError(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS, "ERROR: " + e.toString());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        try {
            logger.log(Thread.currentThread().getName(), "Forget password", LogStepConstant.BEGIN_PROCESS,email);

            if(!adminRepository.existsByEmail(email)){
                logger.log(Thread.currentThread().getName(), "Forget password", LogStepConstant.END_PROCESS,MessageConstant.EMAIL_DOES_NOT_EXIST + " : " +email);
                return ResponseUtil.getResponseEntity("03",MessageConstant.ERROR_03, HttpStatus.BAD_REQUEST);
            }
            Admin admin = adminRepository.findAdminByEmail(email);
            if(admin.getStatus().equals(EStatus.LOCK)){
                logger.log(Thread.currentThread().getName(), "Forget password", LogStepConstant.END_PROCESS,MessageConstant.ERROR_02 + " : " +email);
                return ResponseUtil.getResponseEntity("02",MessageConstant.ERROR_02, HttpStatus.BAD_REQUEST);
            }
            String resetToken = jwtUtil.generateToken(admin);
            redisService.set("KeyResetPassword:" + admin.getAdminId(),resetToken);
            redisService.setTimeToLive("KeyResetPassword:" + admin.getAdminId(),10);
            String resetLink = "http://localhost:8092/reset-password?token=" + resetToken + "&email=" + admin.getEmail();

            ResetPasswordMessage message = ResetPasswordMessage.builder()
                    .email(admin.getEmail())
                    .resetLink(resetLink)
                    .build();

            notificationEventPublisher.sendMailResetPasswordEvent(message);

            logger.log(Thread.currentThread().getName(), "Forget password", LogStepConstant.END_PROCESS,email);
            return ResponseUtil.getResponseEntity("Password reset link sent to your email",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.logError(Thread.currentThread().getName(), "Forget password", LogStepConstant.END_PROCESS,e.toString());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        try{
            logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.BEGIN_PROCESS, "");
            if(!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getReNewPassword())){
                logger.log(Thread.currentThread().getName(), "Change password",LogStepConstant.END_PROCESS,MessageConstant.passwordMismatch);
                return ResponseUtil.getResponseEntity(MessageConstant.passwordMismatch, HttpStatus.BAD_REQUEST);

            }
            if(!isValidPassword(resetPasswordRequest.getNewPassword())){
                logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.END_PROCESS, MessageConstant.INVALID_DATA);
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            if(jwtUtil.validateToken(resetPasswordRequest.getToken())){
                //note

                Claims claims = jwtUtil.extractAllClaims(resetPasswordRequest.getToken());
                String adminId = (String) claims.get("id");
                if(redisService.keyExists("KeyResetPassword:"+adminId) && redisService.getValue("KeyResetPassword:"+adminId).equals(resetPasswordRequest.getToken())){

                    Admin admin = adminRepository.findAdminByAdminId(adminId);
                    admin.setPassword(encoder.encode(resetPasswordRequest.getNewPassword()));
                    adminRepository.save(admin);
                    accountEventPublisher.updateAdminAccountEvent(modelMapper.map(admin,AdminDTO.class));
                    redisService.delete("KeyResetPassword:"+adminId);

                    logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.END_PROCESS, MessageConstant.CHANGED_PASSWORD_SUCCESSFULLY);

                    return ResponseUtil.getResponseEntity(MessageConstant.CHANGED_PASSWORD_SUCCESSFULLY, HttpStatus.OK);
                }else {
                    logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.END_PROCESS, MessageConstant.UNAUTHORIZED);
                    return ResponseUtil.getResponseEntity(MessageConstant.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
                }

            }
            else {
                logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.END_PROCESS, MessageConstant.UNAUTHORIZED);
                return ResponseUtil.getResponseEntity(MessageConstant.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), "Reset password",LogStepConstant.END_PROCESS, e.toString());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateOtherAccount(UpdateOtherAccountRequest request) {
        try{
            logger.log(Thread.currentThread().getName(), "update other account info",LogStepConstant.BEGIN_PROCESS,request.toString());
            Admin admin = adminRepository.findAdminByAdminId(request.getAdminId());
            if(Objects.isNull(admin)){
                logger.log(Thread.currentThread().getName(), "update other account info",LogStepConstant.END_PROCESS,MessageConstant.ADMIN_NOT_EXIST);
                return ResponseUtil.getResponseEntity(MessageConstant.ADMIN_NOT_EXIST, HttpStatus.BAD_REQUEST);
            }
            if(request.getName() == null || request.getRoleGroupId() == null ||request.getName().trim().isEmpty() || request.getRoleGroupId().trim().isEmpty()){
                logger.log(Thread.currentThread().getName(), "update other account info",LogStepConstant.END_PROCESS,MessageConstant.INVALID_DATA);
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            admin.setName(request.getName());
            admin.setRoleGroupId(request.getRoleGroupId());
            adminRepository.save(admin);
            accountEventPublisher.updateAdminAccountEvent(modelMapper.map(admin,AdminDTO.class));

            logger.log(Thread.currentThread().getName(), "update other account info",LogStepConstant.END_PROCESS,MessageConstant.UPDATE_ACCOUNT_SUCCESSFULLY);

            return ResponseUtil.getResponseEntity("00",MessageConstant.UPDATE_ACCOUNT_SUCCESSFULLY,HttpStatus.OK);

        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "update other account info",LogStepConstant.END_PROCESS,"ERROR:"+ e.getMessage());
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateAccountInfo(UpdateAccountInfoRequest updateAccountInfoRequest) {
        try{
            logger.log(Thread.currentThread().getName(), "update account info",LogStepConstant.BEGIN_PROCESS,updateAccountInfoRequest.toString());

            if(updateAccountInfoRequest.getName() == null||updateAccountInfoRequest.getName().isEmpty() ){
                logger.log(Thread.currentThread().getName(), "update account info",LogStepConstant.END_PROCESS,MessageConstant.INVALID_DATA);
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            Admin admin = customAdminDetailsService.getAdminDetail();
            admin.setName(updateAccountInfoRequest.getName());
            adminRepository.save(admin);

            accountEventPublisher.updateAdminAccountEvent(modelMapper.map(admin,AdminDTO.class));

            logger.log(Thread.currentThread().getName(), "update account info",LogStepConstant.END_PROCESS,MessageConstant.UPDATE_ACCOUNT_SUCCESSFULLY);
            return ResponseUtil.getResponseEntity("00",MessageConstant.UPDATE_ACCOUNT_SUCCESSFULLY,HttpStatus.OK);


        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "update account info",LogStepConstant.END_PROCESS,"ERROR:"+ e.getMessage());
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(RegexConstant.PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
