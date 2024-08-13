package co.anbora.labs.ngrok.compatibility;

import com.intellij.execution.services.ServiceViewActionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.remoteServer.impl.runtime.ui.tree.DeploymentNode;
import com.intellij.remoteServer.runtime.Deployment;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApplicationActionUtils {

  public static <T> @Nullable T getApplicationRuntime(@NotNull AnActionEvent e,
                                                      @NotNull Class<T> clazz) {
    Deployment deployment = getDeployment(getDeploymentTarget(e));
    return deployment == null
        ? null
        : ObjectUtils.tryCast(deployment.getRuntime(), clazz);
  }

  public static @Nullable
  DeploymentNode getDeploymentTarget(@NotNull AnActionEvent e) {
    return ServiceViewActionUtils.getTarget(e, DeploymentNode.class);
  }

  public static @Nullable
  Deployment getDeployment(@Nullable DeploymentNode node) {
    return node == null
        ? null
        : ObjectUtils.tryCast(node.getValue(), Deployment.class);
  }
}
