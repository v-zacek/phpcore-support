package action.createfile;

import icons.PhpIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;

public class CreateLogicClassFileAction extends CreateFileFromTemplateAction {

    public CreateLogicClassFileAction() {
        super("LogicClass", "Create new LogicClass", PhpIcons.PhpIcon);
    }

    @Override
    protected void buildDialog(
        @NotNull Project project,
        @NotNull PsiDirectory psiDirectory,
        @NotNull CreateFileFromTemplateDialog.Builder builder
    ) {
        builder.setTitle("New LogicClass")
            .addKind("LogicClass", PhpIcons.PhpIcon, "logicClass.php")
            .setDefaultText("Lg");
    }

    @Override
    protected @NlsContexts.Command String getActionName(
        PsiDirectory psiDirectory,
        @NonNls @NotNull String s,
        @NonNls String s1
    ) {
        return "Creating new LogicClass " + s;
    }
}
