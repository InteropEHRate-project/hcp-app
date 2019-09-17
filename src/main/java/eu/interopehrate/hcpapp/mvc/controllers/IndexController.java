package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.requestprocessors.BluetoothConnectionInfoBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Value("${bluetooth.connection.info.image.size}")
    private String bluetoothConnectionInfoImageSize;
    private BluetoothConnectionInfoBuilder bluetoothConnectionInfoBuilder;

    public IndexController(BluetoothConnectionInfoBuilder bluetoothConnectionInfoBuilder) {
        this.bluetoothConnectionInfoBuilder = bluetoothConnectionInfoBuilder;
    }

    @RequestMapping({"/", "/index"})
    public String indexTemplate(Model model) throws Exception {
        IndexCommand indexCommand = new IndexCommand();
        indexCommand.setBluetoothConnectionInfoImage(bluetoothConnectionInfoBuilder.connectionInfoQRCodePng());
        indexCommand.setBluetoothConnectionInfoImageSize(bluetoothConnectionInfoImageSize);
        model.addAttribute("index", indexCommand);
        return TemplateNames.INDEX_TEMPLATE;
    }
}
