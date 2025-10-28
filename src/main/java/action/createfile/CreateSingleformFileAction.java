package action.createfile;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;

public class CreateSingleformFileAction extends CreateFileFromTemplateAction {

    public CreateSingleformFileAction() {
        super("Singleform", "Create new singleform", AllIcons.FileTypes.Xml);
    }

    @Override
    protected void buildDialog(
        @NotNull Project project,
        @NotNull PsiDirectory psiDirectory,
        @NotNull CreateFileFromTemplateDialog.Builder builder
    ) {
        builder.setTitle("New Singleform")
            .addKind("Form", AllIcons.FileTypes.Xml, "singleform.xml")
            .setDefaultText("sf");
    }

    @Override
    protected @NlsContexts.Command String getActionName(
        PsiDirectory psiDirectory,
        @NonNls @NotNull String s,
        @NonNls String s1
    ) {
        return "Creating new singleform " + s;
    }
}
