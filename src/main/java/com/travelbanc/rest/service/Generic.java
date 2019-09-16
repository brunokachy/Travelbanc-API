/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.rest.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.travelbanc.ProjectConstant;
import com.travelbanc.enums.Gender;
import com.travelbanc.enums.GenericRecordStatus;
import com.travelbanc.enums.ReservationType;
import com.travelbanc.enums.TitleType;
import com.travelbanc.persistence.entity.PaymentHistory;
import com.travelbanc.persistence.entity.PortalUser;
import com.travelbanc.persistence.entity.Reservation;
import com.travelbanc.persistence.entity.ReservationOwner;
import com.travelbanc.persistence.entity.Traveller;
import com.travelbanc.persistence.entity.VerificationToken;
import com.travelbanc.persistence.service.PaymentHistoryRepository;
import com.travelbanc.persistence.service.PortalUserRepository;
import com.travelbanc.persistence.service.ReservationRepository;
import com.travelbanc.persistence.service.ReservationOwnerRespository;
import com.travelbanc.persistence.service.TravellerRepository;
import com.travelbanc.persistence.service.VerificationTokenRepository;
import com.travelbanc.rest.entity.BookingRequest;
import com.travelbanc.rest.entity.ChangePassword;
import com.travelbanc.rest.entity.PaymentVerification;
import com.travelbanc.rest.entity.ResponseObject;
import com.travelbanc.utils.EmailService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("")
public class Generic {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final PortalUserRepository portalUserRepository;
    private final ReservationOwnerRespository reservationOwnerRepository;
    private final TravellerRepository travellerRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final HttpServletRequest request;
    private final EmailService emailService;

    public Generic(ReservationRepository reservationRepository, PortalUserRepository portalUserRepository,
            ReservationOwnerRespository reservationOwnerRepository, TravellerRepository travellerRepository,
            PaymentHistoryRepository paymentHistoryRepository, EmailService emailService, PasswordEncoder passwordEncoder,
            VerificationTokenRepository verificationTokenRepository, HttpServletRequest request) {
        this.reservationRepository = reservationRepository;
        this.portalUserRepository = portalUserRepository;
        this.reservationOwnerRepository = reservationOwnerRepository;
        this.travellerRepository = travellerRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
        this.request = request;
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseObject> test() {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re;
        ro.setData("Dummy Data!!!!!");
        ro.setMessage("Please fill in user details");

        Map<String, Object> test = new HashMap<>();
        test.put("username", "Bruno Okafor");
        test.put("activationLink", "http://mytravelbanc.com");
        emailService.sendHtmlEmail("bruno.okafor@gmail.com", "As you Know it", "account-activated-template", test, "");

        ro.setStatus(ProjectConstant.SUCCESS);
        re = new ResponseEntity<>(ro, HttpStatus.OK);
        return re;
    }

    @PostMapping("/create_account")
    public ResponseEntity<ResponseObject> createAccount(@RequestBody PortalUser portalUser) {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re = null;
        if (portalUser != null) {
            PortalUser pu = portalUserRepository.findByEmail(portalUser.getEmail());
            if (pu != null) {
                if (pu.getStatus() == GenericRecordStatus.PENDING) {
                    try {
                        pu.setDateCreated(new Date());
                        pu.setLastLogin(new Date());
                        pu.setLastUpdated(new Date());
                        pu.setFirstName(portalUser.getFirstName());
                        pu.setLastName(portalUser.getLastName());
                        pu.setPhoneNumber(portalUser.getPhoneNumber());
                        pu.setAddress(portalUser.getAddress());
                        pu.setCityName(portalUser.getCityName());
                        pu.setCountryName(portalUser.getCountryName());
                        pu.setDateOfBirth(portalUser.getDateOfBirth());
                        pu.setPassword(this.passwordEncoder.encode(portalUser.getPassword()));

                        if (portalUser.getTitleCode() == 1) {
                            pu.setTitle(TitleType.MISS);
                        }
                        if (portalUser.getTitleCode() == 3) {
                            pu.setTitle(TitleType.MRS);
                        }

                        if (portalUser.getTitleCode() == 4) {
                            pu.setTitle(TitleType.MR);
                        }

                        portalUserRepository.save(pu);
                        if (portalUser.getPlatform().equalsIgnoreCase("Web")) {
                            generateWebToken(portalUser);
                        }
                        if (portalUser.getPlatform().equalsIgnoreCase("Mobile")) {
                            generateMobileToken(portalUser);
                        }
                        ro.setStatus(ProjectConstant.SUCCESS);
                        ro.setMessage("User account created successfully. Check your email to acitvate your account");
                        re = new ResponseEntity<>(ro, HttpStatus.OK);
                    } catch (Exception e) {
                        e.getMessage();
                        ro.setMessage("User creation was unsucessful. Try again later or contact admin");
                        ro.setData("");
                        re = new ResponseEntity<>(ro, HttpStatus.OK);
                    }

                }
                if (pu.getStatus() == GenericRecordStatus.ACTIVE) {
                    ro.setMessage("An account with this email address " + pu.getEmail() + " already exist");
                    ro.setData("");
                    ro.setStatus(ProjectConstant.FAILURE);
                    re = new ResponseEntity<>(ro, HttpStatus.OK);
                }
            } else {
                try {
                    portalUser.setDateCreated(new Date());
                    portalUser.setLastLogin(new Date());
                    portalUser.setLastUpdated(new Date());
                    portalUser.setStatus(GenericRecordStatus.PENDING);
                    portalUser.setPassword(this.passwordEncoder.encode(portalUser.getPassword()));
                    if (portalUser.getTitleCode() == 1) {
                        portalUser.setTitle(TitleType.MISS);
                    }
                    if (portalUser.getTitleCode() == 3) {
                        portalUser.setTitle(TitleType.MRS);
                    }

                    if (portalUser.getTitleCode() == 4) {
                        portalUser.setTitle(TitleType.MR);
                    }
                    portalUserRepository.save(portalUser);
                    if (portalUser.getPlatform().equalsIgnoreCase("Web")) {
                        generateWebToken(portalUser);
                    }
                    if (portalUser.getPlatform().equalsIgnoreCase("Mobile")) {
                        generateMobileToken(portalUser);
                    }

                    ro.setStatus(ProjectConstant.SUCCESS);
                    ro.setMessage("User account created Successfully. Check your email to acitvate your account");
                    re = new ResponseEntity<>(ro, HttpStatus.OK);
                } catch (Exception e) {
                    e.getMessage();
                    ro.setMessage("User creation was unsucessful. Try again later or contact admin");
                    ro.setData("");
                    ro.setStatus(ProjectConstant.FAILURE);
                    re = new ResponseEntity<>(ro, HttpStatus.OK);
                }
            }
        } else {
            ro.setMessage("Please fill in user details");
            ro.setStatus(ProjectConstant.FAILURE);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        }
        return re;
    }

    private void generateWebToken(PortalUser pu) {
        VerificationToken verificationToken = new VerificationToken();
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        verificationToken.setExpiryDate(dt);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(pu);
        verificationTokenRepository.save(verificationToken);
        String url = ProjectConstant.CLIENTLIVEURL + "account_verification/" + verificationToken.getToken();
        String username = pu.getFirstName() + " " + pu.getLastName();

        Map<String, Object> activationEmail = new HashMap<>();
        activationEmail.put("username", username);
        activationEmail.put("activationLink", url);
        emailService.sendHtmlEmail(pu.getEmail(), "Account Creation Activation", "account-activated-template", activationEmail, ProjectConstant.ACTIVATIONEMAILSENDER);
    }

    private void generateMobileToken(PortalUser pu) {
        VerificationToken verificationToken = new VerificationToken();
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        verificationToken.setExpiryDate(dt);

        Random r = new Random();
        Integer code = (r.ints(100000, 999999).limit(1).findFirst().getAsInt());
        verificationToken.setToken(code.toString());
        verificationToken.setUser(pu);
        verificationTokenRepository.save(verificationToken);
        String msg = +code + " is your Travelbanc activation code.";

        String phoneNumber = pu.getPhoneNumber();
        String regx = "+";
        char[] ca = regx.toCharArray();
        for (char cc : ca) {
            phoneNumber = phoneNumber.replace("" + cc, "");
        }
        System.out.println("phone number " + phoneNumber);
        sendSMS(phoneNumber, msg);
    }

    @PostMapping("/send_app_download_link")
    public ResponseEntity<ResponseObject> sendAppDownloadLink(@RequestBody PortalUser portalUser) {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re;
        Optional<PortalUser> pu = Optional.of(portalUser);
        if (pu.isPresent()) {
            String msg = "Travelbanc Mobile App Link. ANDROID: https://play.google.com/store/apps/details?id=com.travelbanc.app";
            String phoneNumber = pu.get().getPhoneNumber();
            String regx = "+";
            char[] ca = regx.toCharArray();
            for (char cc : ca) {
                phoneNumber = phoneNumber.replace("" + cc, "");
            }
            System.out.println("phone number " + phoneNumber);
            sendSMS(phoneNumber, msg);
            ro.setStatus(ProjectConstant.SUCCESS);
            ro.setMessage("User account created successfully. Check your email to acitvate your account");
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        } else {
            ro.setMessage("User creation was unsucessful. Try again later or contact admin");
            ro.setData("");
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        }
        return re;
    }

    private void sendSMS(String phoneNumber, String msg) {

        Map<String, String> auth = new HashMap<>();
        auth.put("username", ProjectConstant.SMS_USERNAME);
        auth.put("apikey", ProjectConstant.SMS_API_KEY);

        Map<String, String> message = new HashMap<>();
        message.put("sender", "TRAVELBANC");
        message.put("messagetext", msg);
        message.put("flash", "0");

        List<Map<String, String>> gsm = new ArrayList<>();
        Map<String, String> gsm1 = new HashMap<>();
        gsm1.put("msidn", phoneNumber);
        gsm1.put("msgid", "");
        gsm.add(gsm1);

        Map<String, List<Map<String, String>>> recipients = new HashMap<>();
        recipients.put("gsm", gsm);

        JSONObject SMS = new JSONObject();
        SMS.put("auth", auth);
        SMS.put("message", message);
        SMS.put("recipients", recipients);

        JSONObject requestObject = new JSONObject();
        requestObject.put("SMS", SMS);

        System.out.println(requestObject.toString());

        try {
            HttpResponse<JsonNode> response = Unirest.post(ProjectConstant.SMS_URL)
                    .header("Content-Type", "application/json")
                    .body(requestObject)
                    .asJson();

            JsonNode jsonNode = response.getBody();
            Optional<JSONObject> opt = Optional.of(jsonNode.getObject());
            if (opt.isPresent()) {
                System.out.println(opt.get().toString());
            } else {
                try {
                    throw new Exception("No response from server");
                } catch (Exception ex) {
                    Logger.getLogger(Generic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (UnirestException ex) {
            Logger.getLogger(Generic.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @PostMapping("/confirm_registration")
    public ResponseEntity<ResponseObject> confirmRegistration(@RequestBody VerificationToken token) {
        ResponseObject<PortalUser> ro = new ResponseObject();
        VerificationToken verificationToken = this.verificationTokenRepository.findByToken(token.getToken());
        if (verificationToken == null) {
            ro.setMessage("Verification Token does not exist. Create a new account");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
        if (verificationToken.getUser().getStatus() == GenericRecordStatus.ACTIVE) {
            ro.setMessage("User Account Already Verified. Login to continue");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
        if (new Date().getTime() > verificationToken.getExpiryDate().getTime()) {
            ro.setMessage("Verification Token has expired. Create a new account");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
        try {
            Optional<PortalUser> checkUser = portalUserRepository.findById(verificationToken.getUser().getId());
            if (checkUser.isPresent()) {
                PortalUser user = checkUser.get();
                user.setStatus(GenericRecordStatus.ACTIVE);
                user.setLastUpdated(new Date());
                portalUserRepository.save(user);
                ro.setStatus(ProjectConstant.SUCCESS);
                ro.setMessage("Account verification was successful.");
                ro.setData(user);
                return new ResponseEntity<>(ro, HttpStatus.OK);
            } else {
                ro.setMessage("Account verification was unsuccessful. Please contact admin");
                ro.setStatus(ProjectConstant.FAILURE);
                return new ResponseEntity<>(ro, HttpStatus.OK);
            }

        } catch (Exception e) {
            ro.setMessage("Account verification was unsuccessful. Please contact admin");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }

    }

    @PostMapping("/update_profile")
    public ResponseEntity<ResponseObject> updateProfile(@RequestBody PortalUser portalUser) {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re;
        if (portalUser != null) {
            PortalUser pu = portalUserRepository.findByEmail(portalUser.getEmail());
            if (pu != null) {
                try {
                    pu.setLastUpdated(new Date());
                    pu.setFirstName(portalUser.getFirstName());
                    pu.setLastName(portalUser.getLastName());
                    pu.setPhoneNumber(portalUser.getPhoneNumber());
                    pu.setAddress(portalUser.getAddress());
                    pu.setCityName(portalUser.getCityName());
                    pu.setCountryName(portalUser.getCountryName());
                    pu.setDateOfBirth(portalUser.getDateOfBirth());

                    if (portalUser.getTitleCode() == 1) {
                        pu.setTitle(TitleType.MISS);
                    }
                    if (portalUser.getTitleCode() == 3) {
                        pu.setTitle(TitleType.MRS);
                    }

                    if (portalUser.getTitleCode() == 4) {
                        pu.setTitle(TitleType.MR);
                    }
                    portalUserRepository.save(pu);
                    ro.setStatus(ProjectConstant.SUCCESS);
                    ro.setMessage("User account created Successfully. Check your email to acitvate your account");
                    re = new ResponseEntity<>(ro, HttpStatus.OK);
                } catch (Exception e) {
                    e.getMessage();
                    ro.setMessage("Profile update was unsucessful. Try again later or contact admin");
                    ro.setData("");
                    ro.setStatus(ProjectConstant.FAILURE);
                    re = new ResponseEntity<>(ro, HttpStatus.OK);
                }
            } else {
                ro.setMessage("Profile update was unsucessful. Try again later or contact admin");
                ro.setData("");
                ro.setStatus(ProjectConstant.FAILURE);
                re = new ResponseEntity<>(ro, HttpStatus.OK);
            }
        } else {
            ro.setMessage("Please fill in user details");
            ro.setStatus(ProjectConstant.FAILURE);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        }
        return re;
    }

    @PostMapping("/sign_in")
    public ResponseEntity<ResponseObject> login(@RequestBody PortalUser portalUser) {
        ResponseObject<String> ro = new ResponseObject();
        if (portalUser != null) {
            PortalUser pu = null;
            if (portalUser.getEmail() != null) {
                pu = portalUserRepository.findByEmail(portalUser.getEmail());
            }
            if (pu == null) {
                ro.setMessage("User does not exist");
                ro.setStatus(ProjectConstant.FAILURE);
                return new ResponseEntity<>(ro, HttpStatus.OK);
            } else {
                if (pu.getStatus() == GenericRecordStatus.PENDING || pu.getStatus() == GenericRecordStatus.DEACTIVATED) {
                    ro.setMessage("Account is not active. Please contact admin");
                    ro.setStatus(ProjectConstant.FAILURE);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                }

                if (pu.getStatus() == GenericRecordStatus.ACTIVE) {
                    if (this.passwordEncoder.matches(portalUser.getPassword(), pu.getPassword())) {
                        ResponseObject<PortalUser> resp = new ResponseObject();
                        resp.setMessage("Login successful");
                        resp.setData(pu);
                        resp.setStatus(ProjectConstant.SUCCESS);
                        pu.setLastLogin(new Date());
                        portalUserRepository.save(pu);

                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    } else {
                        ro.setMessage("Account password is not valid");
                        ro.setStatus(ProjectConstant.FAILURE);
                        return new ResponseEntity<>(ro, HttpStatus.OK);
                    }
                } else {
                    ro.setMessage("Portal User is not active");
                    ro.setStatus(ProjectConstant.FAILURE);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                }
            }

        } else {
            ro.setMessage("Please provide your email and password");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
    }

    @PostMapping("/change_password")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody ChangePassword cp) {
        ResponseObject<String> ro = new ResponseObject();
        if (cp != null) {
            if (cp.getEmail() != null) {
                PortalUser pu = portalUserRepository.findByEmail(cp.getEmail());
                if (this.passwordEncoder.matches(cp.getCurrentPassword(), pu.getPassword())) {
                    pu.setLastUpdated(new Date());
                    pu.setPassword(this.passwordEncoder.encode(cp.newPassword));
                    portalUserRepository.save(pu);
                    ro.setMessage("Password Change was successful");
                    ro.setStatus(ProjectConstant.SUCCESS);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                } else {
                    ro.setMessage("Password is not valid");
                    ro.setStatus(ProjectConstant.FAILURE);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                }
            }
            ro.setMessage("Please provide your password");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        } else {
            ro.setMessage("Please provide your password");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
    }

    @PostMapping("/reset_password")
    public ResponseEntity<ResponseObject> sendPasswordResetLink(@RequestBody PortalUser portalUser) {
        ResponseObject<String> ro = new ResponseObject();
        if (portalUser != null) {
            if (portalUser.getEmail() != null) {
                PortalUser pu = portalUserRepository.findByEmail(portalUser.getEmail());
                if (pu != null) {
                    String newPassword = UUID.randomUUID().toString();
                    pu.setLastUpdated(new Date());
                    pu.setPassword(this.passwordEncoder.encode(newPassword));
                    portalUserRepository.save(pu);

                    String username = pu.getFirstName() + " " + pu.getLastName();

                    Map<String, Object> resetPasswordEmail = new HashMap<>();
                    resetPasswordEmail.put("username", username);
                    resetPasswordEmail.put("newPassword", newPassword);
                    emailService.sendHtmlEmail(pu.getEmail(), "Password Reset", "reset-password-template", resetPasswordEmail, ProjectConstant.PASSWORDRESETSENDER);

                    ro.setMessage("Password Change was successful");
                    ro.setStatus(ProjectConstant.SUCCESS);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                } else {
                    ro.setMessage("Your email address does not exist!");
                    ro.setStatus(ProjectConstant.FAILURE);
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                }

            }
            ro.setMessage("Please provide your email");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        } else {
            ro.setMessage("Please provide your email");
            ro.setStatus(ProjectConstant.FAILURE);
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
    }

    @PostMapping("/payment_verification")
    public ResponseEntity<ResponseObject> paymentVerification(@RequestBody PaymentVerification paymentVerification) {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re;
        try {
            // This packages the payload
            JSONObject data = new JSONObject();
            data.put("txref", paymentVerification.getFlwRef());
            data.put("SECKEY", ProjectConstant.LIVE_SECERT_KEY);
            data.put("include_payment_entity", paymentVerification.getPaymententity()); // end of payload
            // This sends the request to server with payload
            HttpResponse<JsonNode> response = Unirest.post(ProjectConstant.LIVE_PAYMENT_VERIFY_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .body(data)
                    .asJson();
            // This get the response from payload
            JsonNode jsonNode = response.getBody();
            // This get the json object from payload
            JSONObject responseObject = jsonNode.getObject();
            //   System.out.println(responseObject.toString());
            // check of no object is returned
            if (responseObject == null) {
                throw new Exception("No response from server");
            }
            // This get status from returned payload
            String status = responseObject.optString("status", null);
            // this ensures that status is not null
            if (status == null) {
                throw new Exception("Transaction status unknown");
            }
            // This confirms the transaction exist on rave
            if (!"success".equalsIgnoreCase(status)) {
                String message = responseObject.optString("message", null);
                throw new Exception(message);
            }

            try {
                data = responseObject.getJSONObject("data");
            } catch (JSONException e) {
                JSONArray dataArray = responseObject.getJSONArray("data");
                data = dataArray.getJSONObject(0);
            }

            // This get the amount stored on server
            double actualAmount = data.getDouble("amount");

            // This validates that the amount stored on client is same returned
            if (actualAmount != paymentVerification.getAmount()) {
                throw new Exception("Amount does not match");
            }

            Reservation r = reservationRepository.findByReferenceNumber(paymentVerification.getFlwRef());
            if (r != null) {
                PaymentHistory paymentHistory = new PaymentHistory();
                paymentHistory.setAmount(r.getAmount());
                paymentHistory.setAmountPaid(r.getAmount());
                paymentHistory.setDateCreated(new Date());
                paymentHistory.setDatePaid(new Date());
                paymentHistory.setLastUpdated(new Date());
                paymentHistory.setPayerEmail(r.getOwner().getEmail());
                paymentHistory.setPayerName(r.getOwner().getFirstName() + " " + r.getOwner().getLastName());
                paymentHistory.setPayerPhoneNumber(r.getOwner().getPhoneNumber());
                paymentHistory.setPaymentResponseMessage(responseObject.optString("status", null));
                paymentHistory.setPaymentReference(paymentVerification.getPaymentRef());
                paymentHistory.setPaymentResponseCode(paymentVerification.getPaymentCode());
                PaymentHistory ph = paymentHistoryRepository.save(paymentHistory);

                r.setPaymentHistory(ph);
                reservationRepository.save(r);
            }
            ro.setMessage("Payment Verification was successful");
            ro.setStatus(ProjectConstant.SUCCESS);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
            // now you can give value for payment.
        } catch (UnirestException ex) {
            Logger.getLogger(Generic.class.getName()).log(Level.SEVERE, null, ex);
            ro.setMessage("Payment Verification was not successful");
            ro.setStatus(ProjectConstant.FAILURE);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        } catch (Exception ex) {
            Logger.getLogger(Generic.class.getName()).log(Level.SEVERE, null, ex);
            ro.setMessage("Payment Verification was not successful");
            ro.setStatus(ProjectConstant.FAILURE);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        }
        return re;
    }

    @PostMapping("/booking")
    public ResponseEntity<ResponseObject> booking(@RequestBody BookingRequest bookingReq) {
        ResponseObject<String> ro = new ResponseObject();
        ResponseEntity re;
        Reservation reserve;
        ReservationOwner reservationOwner;
        if (bookingReq != null) {
            try {
                ReservationOwner owner = new ReservationOwner();
                owner.setFirstName(bookingReq.getReservation().getReservationOwner().getFirstName());
                owner.setLastName(bookingReq.getReservation().getReservationOwner().getLastName());
                owner.setAddress(bookingReq.getReservation().getReservationOwner().getAddress());
                owner.setDateOfBirth(bookingReq.getReservation().getReservationOwner().getDateOfBirth());
                owner.setEmail(bookingReq.getReservation().getReservationOwner().getEmail());
                owner.setPhoneNumber(bookingReq.getReservation().getReservationOwner().getPhoneNumber());

                if (bookingReq.getReservation().getReservationOwner().getTitleCode() == 1) {
                    owner.setTitle(TitleType.MISS);
                }
                if (bookingReq.getReservation().getReservationOwner().getTitleCode() == 3) {
                    owner.setTitle(TitleType.MRS);
                }

                if (bookingReq.getReservation().getReservationOwner().getTitleCode() == 0) {
                    owner.setTitle(TitleType.MR);
                }

                PortalUser pu = portalUserRepository.findByEmail(bookingReq.getReservation().getReservationOwner().getEmail());
                if (pu != null) {
                    owner.setPortalUser(pu);
                }
                reservationOwner = reservationOwnerRepository.save(owner);

                Reservation reservation = new Reservation();
                reservation.setAmount(bookingReq.getReservation().getAmount());
                reservation.setBookingNumber(bookingReq.getBookingResponse().getBookingNumber());
                reservation.setDateCreated(new Date());
                reservation.setLastUpdated(new Date());
                reservation.setDescription(bookingReq.getReservation().getDescription());
                reservation.setOwner(reservationOwner);
                reservation.setReferenceNumber(bookingReq.getBookingResponse().getReferenceNumber());
                reservation.setTitle(bookingReq.getReservation().getTitle());
                if (bookingReq.getReservation().getReservationType().equalsIgnoreCase("Flight")) {
                    reservation.setReservationType(ReservationType.FLIGHT);
                }
                if (bookingReq.getReservation().getReservationType().equalsIgnoreCase("Hotel")) {
                    reservation.setReservationType(ReservationType.HOTEL);
                }
                reserve = reservationRepository.save(reservation);

                for (com.travelbanc.rest.entity.ReservationOwner traveller : bookingReq.getReservation().getTravellers()) {
                    Traveller t = new Traveller();
                    t.setBirthday(traveller.getDateOfBirth());
                    t.setFirstName(traveller.getFirstName());
                    t.setLastName(traveller.getLastName());

                    if (traveller.getTitleCode() == 1) {
                        t.setTitle(TitleType.MISS);
                        t.setGender(Gender.FEMALE);
                    }
                    if (traveller.getTitleCode() == 3) {
                        t.setTitle(TitleType.MRS);
                        t.setGender(Gender.FEMALE);
                    }

                    if (traveller.getTitleCode() == 0) {
                        t.setTitle(TitleType.MR);
                        t.setGender(Gender.MALE);
                    }

                    if (traveller.getTitleCode() == 2) {
                        t.setTitle(TitleType.MASTER);
                        t.setGender(Gender.MALE);
                    }

                    t.setReservation(reserve);
                    travellerRepository.save(t);
                }

                ResponseObject<Reservation> resp = new ResponseObject();
                resp.setMessage("Booking was successful");
                resp.setData(reserve);
                resp.setStatus(ProjectConstant.SUCCESS);
                re = new ResponseEntity<>(resp, HttpStatus.OK);
                if (bookingReq.getReservation().getReservationType().equalsIgnoreCase("Flight")) {
                    sendFlightBookingEmail(owner, reserve, bookingReq);
                }
                if (bookingReq.getReservation().getReservationType().equalsIgnoreCase("Hotel")) {
                    sendHotelBookingEmail(owner, reserve, bookingReq);
                }
//                try {
//                    String message = "Booking Details:\n" + "RESERVATION DATE: " + reserve.getDateCreated() + "\n" + "BOOKING NUMBER: " + reserve.getReferenceNumber()
//                            + "\n" + "AMOUNT FOR PAYMENT: " + reserve.getAmount() + "\n" + "CONTACT NAME: " + reservationOwner.getFirstName() + "\n" + "CONTACT EMAIL " + reservationOwner.getEmail();
//
//                    EmailService.sendSimpleMessage(ProjectConstant.BOOKINGADDRESS, reservationOwner.getEmail(), message, "Booking Confirmation: TRAVELBANC");
//                } catch (UnirestException ex) {
//                    ex.printStackTrace();
//                    Logger.getLogger(Generic.class.getName()).log(Level.SEVERE, null, ex);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                ro.setMessage("Booking was not successful");
                ro.setStatus(ProjectConstant.FAILURE);
                re = new ResponseEntity<>(ro, HttpStatus.OK);
            }

        } else {
            ro.setMessage("Booking was not successful");
            ro.setStatus(ProjectConstant.FAILURE);
            re = new ResponseEntity<>(ro, HttpStatus.OK);
        }
        return re;
    }

    private void sendHotelBookingEmail(ReservationOwner owner, Reservation reservation, BookingRequest booking) {
        Map<String, Object> hotelBookingEmail = new HashMap<>();
        hotelBookingEmail.put("recieverName", owner.getTitle().getTitleType() + " " + owner.getLastName());
        hotelBookingEmail.put("reservationDate", reservation.getDateCreated());
        hotelBookingEmail.put("bookingNumber", reservation.getBookingNumber());
        hotelBookingEmail.put("hotelName", reservation.getTitle());
        hotelBookingEmail.put("roomType", reservation.getDescription());
        hotelBookingEmail.put("supplierReference", " BOOKING NOT YET CREATED");
        hotelBookingEmail.put("contactName", owner.getTitle().getTitleType() + " " + owner.getFirstName() + " " + owner.getLastName());
        hotelBookingEmail.put("contactEmail", owner.getEmail());
        hotelBookingEmail.put("contactPhone", owner.getPhoneNumber());
        hotelBookingEmail.put("hotelLocation", booking.getReservation().getHotelLocation());
        hotelBookingEmail.put("checkInDate", booking.getReservation().getCheckinDate());
        hotelBookingEmail.put("checkOutDate", booking.getReservation().getCheckoutDate());

        emailService.sendHtmlEmail(owner.getEmail(), "Hotel Booking Notification", "hotel-booking-confirmation", hotelBookingEmail, ProjectConstant.HOTELEMAILSENDER);
    }

    private void sendFlightBookingEmail(ReservationOwner owner, Reservation reservation, BookingRequest booking) {
        Map<String, Object> flightBookingEmail = new HashMap<>();
        flightBookingEmail.put("recieverName", owner.getTitle().getTitleType() + " " + owner.getLastName());
        flightBookingEmail.put("reservationDate", reservation.getDateCreated());
        flightBookingEmail.put("bookingNumber", reservation.getBookingNumber());
        flightBookingEmail.put("flight", reservation.getTitle());
        flightBookingEmail.put("ticketType", reservation.getDescription());
        flightBookingEmail.put("ticketLimitTime", booking.getReservation().getTicketLimitTime());
        flightBookingEmail.put("amount", reservation.getAmount().toString().substring(0, reservation.getAmount().toString().length() - 2) + "." + reservation.getAmount().toString().substring(reservation.getAmount().toString().length() - 2));
        flightBookingEmail.put("supplierReference", " BOOKING NOT YET CREATED");
        flightBookingEmail.put("contactName", owner.getTitle().getTitleType() + " " + owner.getFirstName() + " " + owner.getLastName());
        flightBookingEmail.put("contactEmail", owner.getEmail());
        flightBookingEmail.put("contactPhone", owner.getPhoneNumber());

        emailService.sendHtmlEmail(owner.getEmail(), "Flight Booking Notification", "flight-booking-confirmation", flightBookingEmail, ProjectConstant.FLIGHTEMAILSENDER);
    }

    @PostMapping("/get_booking")
    public ResponseEntity<ResponseObject> getUserBooking(@RequestBody PortalUser portalUser) {
        ResponseObject<List<Reservation>> ro = new ResponseObject();
        if (portalUser != null) {
            ReservationOwner owner = reservationOwnerRepository.findFirstByEmail(portalUser.getEmail());
            if (owner != null) {
                try {
                    List<Reservation> r = reservationRepository.findTop12ByOwnerEmailOrderByDateCreatedDesc(owner.getEmail());
                    ro.setData(r);
                    ro.setStatus(ProjectConstant.SUCCESS);
                    ro.setMessage("Bookings Fetched Successfully");
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                } catch (Exception e) {
                    e.getMessage();
                    ro.setStatus(ProjectConstant.FAILURE);
                    ro.setMessage("Bookings Fetched Unsuccessfully");
                    ro.setData(new ArrayList<>());
                    return new ResponseEntity<>(ro, HttpStatus.OK);
                }
            } else {
                ro.setStatus(ProjectConstant.FAILURE);
                ro.setMessage("ReservationOwner is null");
                return new ResponseEntity<>(ro, HttpStatus.OK);
            }
        } else {
            ro.setStatus(ProjectConstant.FAILURE);
            ro.setMessage("Portal User is null");
            return new ResponseEntity<>(ro, HttpStatus.OK);
        }
    }

}
