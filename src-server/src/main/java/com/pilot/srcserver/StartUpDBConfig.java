package com.pilot.srcserver;

//import com.pilot.srcserver.entity.Account;
//import com.pilot.srcserver.entity.User;
//import com.pilot.srcserver.repository.AccountRepoJPA;
//import com.pilot.srcserver.repository.AccountRepository;
//import com.pilot.srcserver.repository.UserRepoJPA;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.Random;
//
//@Component
//public class StartUpDBConfig implements CommandLineRunner {
//
//    @Autowired
//    private AccountRepoJPA repo;
//
//    @Autowired
//    private UserRepoJPA uRepo;
//
//    private final Random random = new Random();
//
//    private final User defaultUser = User.builder().id(4).username("john4").password("123456").build();
//
//    @Override
//    public void run(String...args) throws Exception {
//        uRepo.save(defaultUser);
//        for(int i = 0; i < 100; i++){
//            repo.save(Account.builder()
//                    .accountStatus("Open")
//                    .accountType(random.nextInt() == 1 ? "Checking" : "Saving")
//                    .balance(BigDecimal.valueOf(random.nextDouble(50000)))
//                    .user(defaultUser)
//                    .accountNumber(RandomStringUtils.random(10, false, true))
//                    .closedDate(null)
//                    .openDate(Date.valueOf(LocalDate.now()))
//                    .currencyType("CNY")
//                    .build()
//            );
//        }
//    }
//}
