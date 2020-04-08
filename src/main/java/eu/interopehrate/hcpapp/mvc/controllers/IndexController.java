package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.services.index.IndexService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Controller
public class IndexController {
    private IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model) throws Exception {
        model.addAttribute("index", indexService.indexCommand());
        return TemplateNames.INDEX_TEMPLATE;
    }

    @RequestMapping("/index/open-connection")
    public String openConnection() throws CertificateException, SignatureException, NoSuchAlgorithmException, KeyStoreException, OperatorCreationException, NoSuchProviderException, InvalidKeyException, IOException {
        indexService.openConnection();
        indexService.certificate();
        return "redirect:/index";
    }

    @RequestMapping("/index/close-connection")
    public String closeConnection() {
        indexService.closeConnection();
        return "redirect:/index";
    }

    @RequestMapping("/index/stop-listening")
    public String stopListening() {
        return "redirect:/index";
    }
}
