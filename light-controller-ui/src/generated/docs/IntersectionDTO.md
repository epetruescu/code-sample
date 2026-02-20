# IntersectionDTO


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **number** |  | [optional] [default to undefined]
**name** | **string** |  | [optional] [default to undefined]
**active** | **boolean** |  | [optional] [default to undefined]
**lastTransitionTime** | **string** |  | [optional] [default to undefined]
**currentPhaseIndex** | **number** |  | [optional] [default to undefined]
**phases** | [**Array&lt;PhaseDTO&gt;**](PhaseDTO.md) |  | [optional] [default to undefined]
**signalGroups** | [**Array&lt;SignalGroupDTO&gt;**](SignalGroupDTO.md) |  | [optional] [default to undefined]

## Example

```typescript
import { IntersectionDTO } from './api';

const instance: IntersectionDTO = {
    id,
    name,
    active,
    lastTransitionTime,
    currentPhaseIndex,
    phases,
    signalGroups,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
