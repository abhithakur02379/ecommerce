package ecommerce;

import Util.WebDr;

import java.util.HashMap;
import java.util.Map;

public class HomePage_Mappings {

    public static void HomePage_Factory() {

        Map<String, String> HomePage_Objects = new HashMap<>();
        HomePage_Objects.put("labelLogo", "XPATH|//h2[normalize-space()='Practice Form Controls']");//SignIn Link
        HomePage_Objects.put("linkSignIn", "XPATH|//a[normalize-space()='Sign up']");//SignIn Link

        WebDr.page_Objects = HomePage_Objects;
    }
}
