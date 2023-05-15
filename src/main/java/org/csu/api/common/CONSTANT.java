package org.csu.api.common;

public class CONSTANT {

    public static final String LOGIN_USER = "loginUser";

    public interface ROLE{
        int CUSTOMER = 1;
        int ADMIN = 0;
    }

    public interface USER_FIELD{
        String USERNAME = "username";
        String EMAIL = "email";
        String PHONE = "phone";
    }
}
