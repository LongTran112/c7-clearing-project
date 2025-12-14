# Testing Helm Chart Locally

Quick guide for testing the chart before pushing to git.

## Quick Test (Recommended)

Run the test script:

```bash
./test-local.sh
```

This will:
- ✅ Lint the chart
- ✅ Render templates with default values
- ✅ Render templates with ingress enabled
- ✅ Validate YAML syntax
- ✅ Check for unrendered templates
- ✅ Show what resources would be created

## Manual Testing Steps

### 1. Lint the Chart

Check for syntax errors and best practices:

```bash
helm lint .
```

### 2. Render Templates (No Cluster Needed)

See what YAML will be generated:

```bash
# Default values
helm template test-release .

# With custom values
helm template test-release . --set ingress.enabled=true

# Save to file for review
helm template test-release . > rendered.yaml
```

### 3. Test Different Configurations

```bash
# Test with ingress enabled
helm template test-release . --set ingress.enabled=true

# Test with different replica count
helm template test-release . --set deployment.replicas=3

# Test with custom image tag
helm template test-release . --set app.image.tag=v2.0.0

# Test with multiple overrides
helm template test-release . \
  --set ingress.enabled=true \
  --set ingress.hosts[0].host=myapp.local \
  --set service.type=ClusterIP
```

### 4. Validate Rendered YAML

Check if the rendered YAML is valid:

```bash
# Using yq (if installed)
helm template test-release . | yq eval '.' > /dev/null

# Or save and check manually
helm template test-release . > test-output.yaml
# Review test-output.yaml in your editor
```

### 5. Check for Common Issues

```bash
# Look for unrendered templates
helm template test-release . | grep -E '\{\{.*\}\}'

# Should return nothing - if you see template syntax, something's wrong
```

### 6. Test with Dry-Run (If You Have a Cluster)

If you have kubectl access to a cluster:

```bash
# Dry-run install (won't actually deploy)
helm install test-release . --dry-run --debug

# Dry-run upgrade (if already installed)
helm upgrade test-release . --dry-run --debug
```

**Note:** Dry-run still needs cluster access, so `helm template` is better for local testing.

## Testing Specific Scenarios

### Test Ingress Configuration

```bash
helm template test-release . \
  --set ingress.enabled=true \
  --set ingress.className=nginx \
  --set ingress.hosts[0].host=test.local
```

### Test Rate Limiting

```bash
helm template test-release . \
  --set ingress.enabled=true \
  --set ingress.rateLimit.rps=5 \
  --set ingress.rateLimit.rpm=50
```

### Test ServiceMonitor

```bash
# Disable servicemonitor
helm template test-release . --set servicemonitor.enabled=false

# Should only show Deployment and Service
```

## Pre-Push Checklist

Before pushing to git, make sure:

- [ ] `helm lint .` passes
- [ ] `helm template` renders without errors
- [ ] No unrendered `{{ }}` templates in output
- [ ] YAML syntax is valid
- [ ] Tested with ingress enabled/disabled
- [ ] Reviewed rendered YAML looks correct

## Quick Commands Reference

```bash
# Full test suite
./test-local.sh

# Just lint
helm lint .

# Render and review
helm template test-release . | less

# Render with ingress
helm template test-release . --set ingress.enabled=true

# Check what resources are created
helm template test-release . | grep "^kind:"
```

## Troubleshooting

**Lint fails:**
- Check template syntax (no spaces in `{{ }}`)
- Verify all required values are defined
- Check YAML indentation

**Template rendering fails:**
- Check if values exist in values.yaml
- Verify template variable names match
- Check for typos in `.Values.*` references

**Unrendered templates in output:**
- Check conditional logic (`{{- if }}`)
- Verify value paths are correct
- Make sure all required values are provided

