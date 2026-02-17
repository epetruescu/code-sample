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

print("Geting the helm repo for redis")

helm_remote('redis',
    repo_name='bitnami',
    repo_url='https://charts.bitnami.com/bitnami',
    values=['./redis-values.yaml'])

k8s_resource(workload="redis-master",
    new_name="redis",
    port_forwards=[6379],
    labels='database')


print('Deploying docker build')

# Build only when manually triggered - ignores source file changes
docker_build(
  'light-controller-ui-image',
  '.',
  ignore=['src/', 'gradle/', '.gradle/', '.git/', '.idea/', '.kiro/']
)

k8s_yaml('deployment.yaml')
# Not used yet
k8s_yaml('loadbalancer.yaml')

k8s_resource('light-controller-ui', 
    port_forwards=[8080],
    trigger_mode=TRIGGER_MODE_MANUAL)