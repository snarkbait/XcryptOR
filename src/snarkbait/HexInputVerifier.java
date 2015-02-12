/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snarkbait;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author tim
 */
public class HexInputVerifier extends InputVerifier
{
    @Override
    public boolean verify(JComponent input)
    { 
        String text = ((JTextField) input).getText();
        String valid = "0123456789abcdefABCDEF";
        boolean e = ((JTextField) input).isEditable();
        if (!e)
        {
            return true;
        }
        if (text.length() == 8)
        {
            for (int i = 0; i < text.length(); i++) {
                if (!valid.contains(text.substring(i, i + 1)))
                {
                    return false;
                }
                
            }
            return true;
        }
        return false;
    }
        
}