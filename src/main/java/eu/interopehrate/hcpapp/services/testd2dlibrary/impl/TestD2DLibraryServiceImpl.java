package eu.interopehrate.hcpapp.services.testd2dlibrary.impl;

import eu.interopehrate.hcpapp.mvc.commands.TestD2DLibraryCommand;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.springframework.stereotype.Service;

@Service
public class TestD2DLibraryServiceImpl implements TestD2DLibraryService {
    private TestD2DLibraryCommand testD2DLibraryCommand = new TestD2DLibraryCommand();

    @Override
    public TestD2DLibraryCommand currentState() {
        return this.testD2DLibraryCommand;
    }

    @Override
    public void openConnection() throws Exception {
        testD2DLibraryCommand.setOn(Boolean.TRUE);
    }

    @Override
    public void closeConnection() throws Exception {
        testD2DLibraryCommand.setOn(Boolean.FALSE);
    }
}
