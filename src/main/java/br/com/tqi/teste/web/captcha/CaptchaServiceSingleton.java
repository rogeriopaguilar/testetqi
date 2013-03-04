package br.com.tqi.teste.web.captcha;

import java.awt.Color;


import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Implementação do mecanismo com o jcaptcha, conforme descrito em:
 * https://jcaptcha.atlassian.net/wiki/display/general/Simple+Servlet+Integration+documentation
 * 
 * @author Rogério de Paula Aguilar
 * */
public class CaptchaServiceSingleton {
 
    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();
 
    public static ImageCaptchaService getInstance(){
    	//instance.
        return instance;
    }
    
    public static boolean captchaIsValid(String captchaId, String response) {
        boolean valido = false; 
    	try {
             valido = getInstance().validateResponseForID(captchaId, response);
         } catch (CaptchaServiceException e) {
              //should not happen, may be thrown if the id is not valid
         }
    	return valido;
    }
    
}

/*Especializando um Engine para gerar apenas números*/

class EngineNumeros extends ListImageCaptchaEngine {

      protected void buildInitialFactories() {
               TextPaster textPaster = new RandomTextPaster(5, 8, Color.white);
               BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(100, 50);
               FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(25, 30);
               WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
               this.addFactory(new GimpyFactory(new RandomWordGenerator("0123456789"), wordToImage));
      }


}


