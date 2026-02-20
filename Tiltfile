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

docker_build('light-controller-ui-image','.',ignore=['.git/', '.idea/', 'light-controller-ui/'])

docker_build('light-controller-frontend','./light-controller-ui',dockerfile='./light-controller-ui/Dockerfile',
  live_update=[sync('./light-controller-ui/src', '/app/src'), run('npm install',
  trigger=['./light-controller-ui/package.json'])]
)

k8s_yaml('deployment.yaml')
k8s_yaml('./light-controller-ui/frontend-deployment.yaml')
# Not used yet
k8s_yaml('loadbalancer.yaml')

k8s_resource('light-controller-ui', port_forwards=[8080], trigger_mode=TRIGGER_MODE_MANUAL, labels='backend')

k8s_resource('light-controller-frontend',  port_forwards=[5173], labels='frontend')