package io.github.hvogel.clientes.util;

import org.springframework.stereotype.Component;

@Component
public class ValidDouble {
	
	public boolean isNumericValid(String strNum) {
        try {
           strNum = strNum.replace(",", ".");	
           Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException npe) {
            return false;
        }
        return true;
    }
}
