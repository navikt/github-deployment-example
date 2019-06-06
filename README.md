# Example using deployment-cli
This example only documents usage of deployment-cli for deployments in a pipeline,
[the documentation for deployment-cli can be found here](https://github.com/nais/doc/blob/master/content/deploy/deployment-cli.md).

## Creating a deployment
Since you need to authenticate to create a Github Deployment you need to provide a way of authenticating. deployment-cli
natively supports deployments as a [Github Apps](https://developer.github.com/apps/) or using a username and password.

In the example we use Github Apps to generate a access token using the environment variables `GITHUB_APP_ID` and 
`GITHUB_APP_KEY_BASE64`. See the documentation for 
[deployment-cli under Authentication for more info](https://github.com/nais/doc/blob/master/content/deploy/deployment-cli.md#Authentication).

If you want to do a deployment locally you can use the following command:

```bash
deployment-cli deploy create --cluster=dev-fss --repository=navikt/github-deployment-example --vars=config-dev.json --team=<teamname> -r=naiserator.yaml --version=<tag pushed to docker registry>
```

This will ask you for a password, since navikt enables two factor authentication you need to provide a 
[personal access token](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line) that
has the scope `repo_deployment`. The token can be created at: https://github.com/settings/tokens.

## Dumping the payload to console or file
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