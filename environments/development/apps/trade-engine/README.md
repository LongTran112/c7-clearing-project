# Trade Engine Helm Chart

Quick reference for deploying the trade engine app.

## Prerequisites

- Kubernetes cluster (we're using K3s)
- Helm 3.x
- NGINX or Traefik ingress controller

## Quick Start

```bash
helm install trade-engine ./k8s-config
```

With custom values:

```bash
helm install trade-engine ./k8s-config -f custom-values.yaml
```

Enable ingress:

```bash
helm install trade-engine ./k8s-config \
  --set ingress.enabled=true \
  --set ingress.hosts[0].host=your-domain.com \
  --set ingress.rateLimit.rps=20
```

## Rate Limiting

Configure in `values.yaml`:

```yaml
ingress:
  enabled: true
  rateLimit:
    enabled: true
    rps: "10" # requests per second
    rpm: "100" # requests per minute
    connections: "10"
    burst: "20"
    whitelist: "1.2.3.4/32" # trusted IPs
```

## Service Types

- **NodePort**: direct access (default, good for dev)
- **ClusterIP**: use with ingress (better for prod)

Switch to ClusterIP when using ingress:

```bash
helm upgrade trade-engine ./k8s-config \
  --set service.type=ClusterIP \
  --set ingress.enabled=true
```

## Ingress Setup

### NGINX

Install it first:

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml
```

Then set `className: nginx` in values.yaml

### Traefik

K3s has this built-in, just set `className: traefik`

## Rate Limit Examples

Strict (for APIs):

```yaml
rateLimit:
  rps: "5"
  rpm: "60"
  connections: "5"
  burst: "10"
```

More lenient (public APIs):

```yaml
rateLimit:
  rps: "50"
  rpm: "1000"
  connections: "20"
  burst: "100"
```

With IP whitelist:

```yaml
rateLimit:
  rps: "10"
  whitelist: "192.168.1.0/24,10.0.0.0/8"
```

## Common Commands

Upgrade:

```bash
helm upgrade trade-engine ./k8s-config
```

Uninstall:

```bash
helm uninstall trade-engine
```

## Troubleshooting

Check ingress:

```bash
kubectl get ingress
kubectl describe ingress trade-engine-ingress
```

Check rate limiting (nginx):

```bash
kubectl logs -n ingress-nginx -l app.kubernetes.io/component=controller
```

Test it:

```bash
for i in {1..20}; do curl -I http://your-domain.com; done
```

### Common Problems

- **429 errors**: rate limit is working, might need to bump `rps`/`rpm`
- **Ingress not working**: make sure ingress controller is running and className matches
- **Can't reach service**: use `ClusterIP` service type with ingress, not `NodePort`
