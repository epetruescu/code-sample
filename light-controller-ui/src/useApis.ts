import { Configuration, IntersectionControllerApi, PhaseControllerApi, SignalGroupControllerApi } from '@/generated'

const config = new Configuration({ basePath: 'http://localhost:8080' })

export const useApi = () => ({
  intersectionApi: new IntersectionControllerApi(config),
  phaseApi: new PhaseControllerApi(config),
  signalGroupApi: new SignalGroupControllerApi(config)
})