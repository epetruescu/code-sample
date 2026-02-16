print('Starting Tiltfile')

allow_k8s_contexts('rancher-desktop')
load('ext://helm_remote', 'helm_remote')

print("Geting the helm repo for postgresql")

helm_remote('postgresql',
    repo_name='bitnami',
    repo_url='https://charts.bitnami.com/bitnami',
    values=['./postgresql-values.yaml'])

k8s_resource(workload="postgresql",
    port_forwards=[5432],
    labels='database')


print('Deploying docker build')

docker_build(
  'light-controller-ui-image',
  '.',
  live_update=[
    sync('./target/classes', '/app/target/classes'),
    run('touch /app/app.jar')
  ]
)

k8s_yaml('deployment.yaml')
# Not used yet
k8s_yaml('loadbalancer.yaml')

k8s_resource('light-controller-ui', port_forwards=[8080])