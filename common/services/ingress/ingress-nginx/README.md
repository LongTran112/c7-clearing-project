# NGINX Ingress Controller

ArgoCD Application for managing NGINX Ingress Controller via GitOps.

## Installation

### Via ArgoCD UI

1. Go to Applications → New App
2. Use the settings from `application.yaml`
3. Or apply directly via kubectl

### Via kubectl

```bash
kubectl apply -f application.yaml
```

## Configuration

The Application installs NGINX Ingress Controller from the official Helm chart with:
- LoadBalancer service type
- Resource limits configured
- Auto-sync enabled

## Customization

Edit `application.yaml` to customize:
- Chart version (`targetRevision`)
- Service type (LoadBalancer, NodePort, etc.)
- Resource limits
- Additional Helm values

## After Installation

Check status:
```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
```

Get the ingress IP:
```bash
kubectl get svc ingress-nginx-controller -n ingress-nginx
```

## Notes

- This uses the official NGINX Ingress Controller Helm chart
- Managed entirely via GitOps through ArgoCD
- Changes to application.yaml will auto-sync
- Once installed, your trade-engine ingress will get an address and rate limiting will work




