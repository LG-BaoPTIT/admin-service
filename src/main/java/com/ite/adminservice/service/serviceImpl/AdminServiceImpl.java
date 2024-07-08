package com.ite.adminservice.service.serviceImpl;

import com.ite.adminservice.config.SystemLogger;
import com.ite.adminservice.constants.LogStepConstant;
import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.constants.RegexConstant;
import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.entities.EStatus;
import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.entities.RoleGroup;
import com.ite.adminservice.event.eventProducer.AccountEventPublisher;
import com.ite.adminservice.payload.dto.AdminDTO;
import com.ite.adminservice.payload.request.RegisterAdminRequest;
import com.ite.adminservice.payload.request.SearchAccountRequest;
import com.ite.adminservice.payload.response.AdminDetailResponse;
import com.ite.adminservice.payload.response.AdminResponse;
import com.ite.adminservice.payload.response.AdminSearchResult;
import com.ite.adminservice.payload.response.MyAccountDetailResponse;
import com.ite.adminservice.repositories.AdminRepository;
import com.ite.adminservice.repositories.PermissionRepository;
import com.ite.adminservice.repositories.RoleGroupRepository;
import com.ite.adminservice.security.CustomAdminDetailsService;
import com.ite.adminservice.security.tfa.TfaService;
import com.ite.adminservice.service.AdminService;
import com.ite.adminservice.service.RoleGroupService;
import com.ite.adminservice.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SystemLogger logger;

    @Autowired
    private TfaService tfaService;

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AccountEventPublisher accountEventPublisher;

    @Override
    public ResponseEntity<?> signup(RegisterAdminRequest request) {
        //logger.log(Thread.currentThread().getName(), null,"signup admin",);
        logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.BEGIN_PROCESS,request.toString());
        try {
            if(validateSignUp(request)){

                if (adminRepository.existsByEmail(request.getEmail())) {
                    logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_PROCESS,"Email is already registered");
                    return ResponseUtil.getResponseEntity("01","Email is already registered", HttpStatus.BAD_REQUEST);
                }

                if (adminRepository.existsByPhone(request.getPhone())) {
                    logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_PROCESS,"Phone is already registered");

                    return ResponseUtil.getResponseEntity("02","Phone number is already registered", HttpStatus.BAD_REQUEST);
                }
                Admin admin = modelMapper.map(request,Admin.class);
                admin.setPassword(encoder.encode(request.getPassword()));
                admin.setRegistrationDate(Instant.now());
                admin.setStatus(EStatus.PENDING_APPROVAL);
                admin.setSecret(tfaService.generateNewSecret());
                RoleGroup roleGroup = roleGroupRepository.findRoleGroupByName("STAFF");
                admin.setRoleGroupId(roleGroup.getRoleGroupId());
                admin.setVerified(false);
                logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.BEGIN_CALL_DATABASE,admin.toString());
                adminRepository.save(admin);
                logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_CALL_DATABASE,"Save admin account successful");
                logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_PROCESS,"Signup account successful");
                accountEventPublisher.createAdminEvent(modelMapper.map(admin, AdminDTO.class));

                return ResponseUtil.getResponseEntity("Signup account successful", HttpStatus.OK);

            }else {
                logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_PROCESS,MessageConstant.INVALID_DATA);
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), "Signup admin account", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getListAdmins() {
        logger.log(Thread.currentThread().getName(), "Get list admins", LogStepConstant.BEGIN_PROCESS,"");
        try{
            Admin adminx = customAdminDetailsService.getAdminDetail();
            List<Admin> admins = adminRepository.findAll();

            List<AdminResponse> result = admins.stream()
                    .filter(admin -> !admin.getAdminId().equals(adminx.getAdminId()))
                    .map(admin -> modelMapper.map(admin, AdminResponse.class))
                    .collect(Collectors.toList());
            logger.log(Thread.currentThread().getName(), "Get list admins", LogStepConstant.END_PROCESS,"");

            return ResponseEntity.ok(result);
        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Get list admins", LogStepConstant.END_PROCESS,e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<?> getAdminDetail(String id) {
        try{
            logger.log(Thread.currentThread().getName(), "Get admin detail by id", LogStepConstant.BEGIN_PROCESS,"");

            Admin admin = adminRepository.findAdminByAdminId(id);
            AdminDetailResponse result = modelMapper.map(admin,AdminDetailResponse.class);
            result.setRoleGroupName(roleGroupService.getRoleGroupById(admin.getRoleGroupId()).getName());
            List<Permission> permissions = roleGroupService.getRoleGroupById(admin.getRoleGroupId()).getPermissionIds().stream()
                    .map(permissionId -> permissionRepository.findPermissionByPermissionId(permissionId))
                    .collect(Collectors.toList());

            result.setPermissions(permissions);

            logger.log(Thread.currentThread().getName(), "Get admin detail by id", LogStepConstant.BEGIN_PROCESS,result.getAdminId());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Get admin detail by id", LogStepConstant.END_PROCESS,e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<?> getMyAccountDetail() {
        try{
            Admin admin = customAdminDetailsService.getAdminDetail();
            MyAccountDetailResponse response = MyAccountDetailResponse.builder()
                    .adminId(admin.getAdminId())
                    .name(admin.getName())
                    .email(admin.getEmail())
                    .phone(admin.getPhone())
                    .registrationDate(admin.getRegistrationDate())
                    .roleGroupName(roleGroupRepository.findRoleGroupByRoleGroupId(admin.getRoleGroupId()).getName())
                    .build();

            return ResponseEntity.ok(response);

        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Get my account detail", LogStepConstant.END_PROCESS,e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> searchAdminsByStatusNameOrPhone(SearchAccountRequest searchAccountRequest) {
        logger.log(Thread.currentThread().getName(), "Search admins", LogStepConstant.BEGIN_PROCESS,"");
        try {
            Pageable pageable = (Pageable) PageRequest.of(searchAccountRequest.getPage(), searchAccountRequest.getSize(), Sort.by("status").ascending());
            Admin adminx = customAdminDetailsService.getAdminDetail();
            Page<Admin> adminPage;
            if (searchAccountRequest.isAllStatus()) {
                if (searchAccountRequest.getKeyWord() != null && !searchAccountRequest.getKeyWord().isEmpty()) {
                    adminPage = adminRepository.findByKeyword(searchAccountRequest.getKeyWord(), pageable);
                } else {
                    adminPage = adminRepository.findAll(pageable);//ok
                }
            } else {
                if (searchAccountRequest.getKeyWord() != null && !searchAccountRequest.getKeyWord().isEmpty()) {
                    adminPage = adminRepository.findByKeywordAndStatus(searchAccountRequest.getKeyWord(), searchAccountRequest.getStatus(), pageable);
                } else {
                    adminPage = adminRepository.findByStatus(searchAccountRequest.getStatus(), pageable);//ok
                }
            }

            long totalRecords = adminPage.getTotalElements();

            List<AdminResponse> result = adminPage.getContent().stream()
                    .map(admin -> modelMapper.map(admin, AdminResponse.class))
                    .collect(Collectors.toList());

            int currentPageSize = result.size();
            logger.log(Thread.currentThread().getName(), "Search admins ", LogStepConstant.END_PROCESS,"");

            AdminSearchResult searchResult = new AdminSearchResult(totalRecords,currentPageSize, result);

            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), "Search admins", LogStepConstant.END_PROCESS,e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private boolean validateSignUp(RegisterAdminRequest request){
        if(!request.getPassword().equals(request.getRePassword())){
            return false;
        }
        if (StringUtils.isEmpty(request.getEmail())) {
            log.info("Email is required");
            return false;
        }

        if (StringUtils.isEmpty(request.getPassword())) {
            log.info("Password is required");
            return false;
        }

        if (StringUtils.isEmpty(request.getName())) {
            log.info("Name is required");
            return false;
        }

        if (StringUtils.isEmpty(request.getPhone())) {
            log.info("Phone number is required");
            return false;
        }
        if (!isValidEmail(request.getEmail())) {
            log.info("Email is not valid");
            return false;
        }

        if (!isValidPassword(request.getPassword())) {
            log.info("Password is not strong enough");
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(RegexConstant.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(RegexConstant.PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}

