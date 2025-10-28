package action.createfile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;

public class CreateMultiformFileAction extends CreateFileFromTemplateAction {

    public CreateMultiformFileAction() {
        super("Multiform", "Create new Multiform", AllIcons.FileTypes.Xml);
    }

    @Override
    protected void buildDialog(
        @NotNull Project project,
        @NotNull PsiDirectory psiDirectory,
        @NotNull CreateFileFromTemplateDialog.Builder builder
    ) {
        builder.setTitle("New Multiform")
            .addKind("Form", AllIcons.FileTypes.Xml, "multiform.xml")
            .setDefaultText("mf");
    }

    @Override
    protected @NlsContexts.Command String getActionName(
        PsiDirectory psiDirectory,
        @NonNls @NotNull String s,
        @NonNls String s1
    ) {
        return "Creating new Multiform " + s;
    }
}
