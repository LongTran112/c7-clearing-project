#!/bin/bash
# Quick test script for Helm chart before pushing to git

set -e

echo "🔍 Testing Helm chart..."
echo ""

# 1. Lint the chart
echo "1️⃣  Linting chart..."
helm lint . || {
    echo "❌ Lint failed!"
    exit 1
}
echo "✅ Lint passed"
echo ""

# 2. Template with default values
echo "2️⃣  Rendering templates with default values..."
helm template test-release . > /tmp/rendered-default.yaml
echo "✅ Rendered to /tmp/rendered-default.yaml"
echo ""

# 3. Template with ingress enabled
echo "3️⃣  Rendering templates with ingress enabled..."
helm template test-release . --set ingress.enabled=true > /tmp/rendered-ingress.yaml
echo "✅ Rendered to /tmp/rendered-ingress.yaml"
echo ""

# 4. Validate YAML syntax
echo "4️⃣  Validating YAML syntax..."
if command -v yq &> /dev/null; then
    yq eval '.' /tmp/rendered-default.yaml > /dev/null && echo "✅ Default YAML is valid"
    yq eval '.' /tmp/rendered-ingress.yaml > /dev/null && echo "✅ Ingress YAML is valid"
else
    echo "⚠️  yq not installed, skipping YAML validation"
fi
echo ""

# 5. Check for common issues
echo "5️⃣  Checking for common issues..."
if grep -q "{{.*}}" /tmp/rendered-default.yaml; then
    echo "❌ Found unrendered template variables!"
    grep "{{.*}}" /tmp/rendered-default.yaml
    exit 1
fi
echo "✅ No unrendered templates found"
echo ""

# 6. Show what would be deployed
echo "6️⃣  Resources that would be created:"
helm template test-release . | grep "^kind:" | sort | uniq -c
echo ""

echo "✅ All tests passed! Chart is ready to push."
echo ""
echo "💡 Tip: Review rendered YAML files in /tmp/rendered-*.yaml"

