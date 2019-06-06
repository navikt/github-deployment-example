# Example using deployment-cli
This example only documents usage of deployment-cli for deployments in a pipeline, for the full documentation check out
[the documentation for deployment-cli](../../deployment-cli.md)
## Creating a deployment

## Dumping the payload to stdout or file
Since you might not always want to deploy to verify changes in the template/vars we also have the subcommand
`deploy payload`. This will dump the payload that would be used against the github deployment api so you can see if
everything is templated as expected. By default it will print the config to stdout, but it can also be dumped to a file
using `--outputfile=<filename>.json`. Example: 

```bash
deployment-cli deploy payload --team=plattform --version=1.0.0 --vars=vars-prod-gcp.json --resource=nais.yaml --cluster=dev-fss
```

Would result in:

```json
{
  "ref": "master",
  "description": "Automated deployment request to dev-fss",
  "environment": "dev-fss",
  "payload": {
    "version": [1, 0, 0],
    "team": "plattform",
    "kubernetes": {
      "resources": [
        {
          "apiVersion": "nais.io/v1alpha1",
          "kind": "Application",
          "metadata": {
            "labels": {
              "team": "plattform"
            },
            "name": "testapp",
            "namespace": "default"
          },
          "spec": {
            "image": "navikt/testapp:1.0.0",
            "istio": {
              "enabled": false
            },
            "leaderElection": false,
            "liveness": {
              "failureThreshold": 30,
              "initialDelay": 5,
              "path": "/is_alive",
              "periodSeconds": 5,
              "timeout": 1
            },
            "port": 8080,
            "prometheus": {
              "enabled": true,
              "path": "/prometheus"
            },
            "readiness": {
              "failureThreshold": 30,
              "initialDelay": 5,
              "path": "/is_alive",
              "periodSeconds": 5,
              "timeout": 1
            },
            "replicas": {
              "cpuThresholdPercentage": 70,
              "max": 4,
              "min": 1
            },
            "resources": {
              "env": {
                "name": "TEST_KEY",
                "value": "this_is_production"
              },
              "limits": {
                "cpu": "400m",
                "memory": "512Mi"
              },
              "requests": {
                "cpu": "50m",
                "memory": "128Mi"
              }
            }
          }
        }
      ]
    }
  },
  "required_contexts": []
}
```