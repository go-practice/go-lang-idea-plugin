package ro.redeul.google.go.runner.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;
import ro.redeul.google.go.runner.GAEApplicationConfiguration;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtoader
 * Date: Aug 19, 2010
 * Time: 2:59:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAERunConfigurationEditor extends SettingsEditor<GAEApplicationConfiguration> {

    @Override
    protected void resetEditorFrom(GAEApplicationConfiguration s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void applyEditorTo(GAEApplicationConfiguration s) throws ConfigurationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void disposeEditor() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
