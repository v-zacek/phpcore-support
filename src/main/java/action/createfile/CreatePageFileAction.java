package action.createfile;

import icons.PhpIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;

public class CreatePageFileAction extends CreateFileFromTemplateAction {

    public CreatePageFileAction() {
        super("Page", "Create new Page", PhpIcons.PhpIcon);
    }

    @Override
    protected void buildDialog(
        @NotNull Project project,
        @NotNull PsiDirectory psiDirectory,
        @NotNull CreateFileFromTemplateDialog.Builder builder
    ) {
        builder.setTitle("New Page")
            .addKind("Page", PhpIcons.PhpIcon, "page.php")
            .setDefaultText("Pg");
    }

    @Override
    protected @NlsContexts.Command String getActionName(
        PsiDirectory psiDirectory,
        @NonNls @NotNull String s,
        @NonNls String s1
    ) {
        return "Creating new Page " + s;
    }

}
