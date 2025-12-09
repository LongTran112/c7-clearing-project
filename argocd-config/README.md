# ArgoCD Configuration

Helm values file for managing ArgoCD installation with resource limits and best practices.

## Installation

### First Time Setup

```bash
# Add ArgoCD Helm repo
helm repo add argo https://argoproj.github.io/argo-helm
helm repo update

# Create namespace
kubectl create namespace argocd

# Install ArgoCD
helm upgrade --install argocd argo/argo-cd \
  -n argocd \
  -f values.yaml
```

### Upgrade Existing Installation

```bash
helm upgrade argocd argo/argo-cd \
  -n argocd \
  -f values.yaml
```

## Configuration

### Resource Limits

The `values.yaml` includes resource limits to prevent:

- **Disk issues**: Limits git operations to 2 concurrent (via `reposerver.parallelism.limit`)
- **Memory/CPU issues**: Sets reasonable limits for all components

### Service Access

- **HTTP**: `http://<node-ip>:32674`
- **HTTPS**: `https://<node-ip>:30000`

### Get Admin Password

```bash
kubectl -n argocd get secret argocd-initial-admin-secret \
  -o jsonpath="{.data.password}" | base64 -d; echo
```

## Customization

Edit `values.yaml` to customize:

- ArgoCD version (change `global.image.tag`)
- Resource limits
- Service type (NodePort, LoadBalancer, or ClusterIP with Ingress)
- Ingress configuration

## Self-Management (GitOps)

After the initial manual installation, you can configure ArgoCD to manage itself via GitOps.

### Setup Self-Management

1. **Bootstrap ArgoCD manually** (one-time):

   ```bash
   helm upgrade --install argocd argo/argo-cd -n argocd -f values.yaml
   ```

2. **Add Helm repository to ArgoCD**:

   ```bash
   # Via ArgoCD CLI
   argocd repo add https://argoproj.github.io/argo-helm --type helm --name argo-helm

   # Add your Git repository (for values from Git)
   argocd repo add https://github.com/LongTran112/c7-clearing-project.git --name c7-clearing-project
   ```

3. **Choose an Application manifest**:

   **Option A: Simple (Helm repo only)** - Use `application-simple.yaml`:

   - Uses Helm chart from repo
   - Values managed separately or inline
   - Most compatible

   **Option B: Advanced (Multi-source)** - Use `application-advanced.yaml`:

   - Uses Helm chart from repo + values from Git
   - Requires ArgoCD v2.6+
   - Full GitOps for values

   **Option C: Current** - Use `application.yaml`:

   - Updated format, may need adjustment based on ArgoCD version

4. **Update the chosen application file**:

   - Change `targetRevision: development` to your branch name if needed
   - Update repo URL if different

5. **Apply the Application**:

   ```bash
   # For simple approach
   kubectl apply -f application-simple.yaml

   # OR for advanced (multi-source)
   kubectl apply -f application-advanced.yaml

   # OR use the main one
   kubectl apply -f application.yaml
   ```

6. **ArgoCD will now manage itself**:
   - Push changes to `values.yaml` → ArgoCD auto-updates
   - Self-healing: manual changes revert to match Git
   - All future changes go through Git

### How It Works

- The `application.yaml` creates an ArgoCD Application that points to this directory
- When you push changes to `values.yaml`, ArgoCD detects and applies them
- ArgoCD runs: `helm upgrade argocd argo/argo-cd -f values.yaml`
- This enables full GitOps for ArgoCD itself

### Update Branch/Repo

Edit `application.yaml` to change:

- `targetRevision`: Your Git branch (main, master, development, etc.)
- `repoURL`: Your Git repository URL

## Notes

- Resource limits are set based on best practices to prevent resource exhaustion
- The parallelism limit prevents multiple large git clones from filling disk
- All components have both requests and limits defined
- After self-management is enabled, all ArgoCD changes should go through Git
