# PhaseControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createPhase**](#createphase) | **POST** /phase | |
|[**deletePhase**](#deletephase) | **DELETE** /phase/{id} | |
|[**getPhase**](#getphase) | **GET** /phase/{id} | |
|[**getPhases**](#getphases) | **GET** /phase | |
|[**updatePhase**](#updatephase) | **PUT** /phase/{id} | |

# **createPhase**
> PhaseDTO createPhase(phaseCreateRequest)


### Example

```typescript
import {
    PhaseControllerApi,
    Configuration,
    PhaseCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PhaseControllerApi(configuration);

let phaseCreateRequest: PhaseCreateRequest; //

const { status, data } = await apiInstance.createPhase(
    phaseCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **phaseCreateRequest** | **PhaseCreateRequest**|  | |


### Return type

**PhaseDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deletePhase**
> deletePhase()


### Example

```typescript
import {
    PhaseControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PhaseControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deletePhase(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getPhase**
> PhaseDTO getPhase()


### Example

```typescript
import {
    PhaseControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PhaseControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getPhase(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**PhaseDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getPhases**
> Array<PhaseDTO> getPhases()


### Example

```typescript
import {
    PhaseControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PhaseControllerApi(configuration);

let intersectionId: number; // (default to undefined)

const { status, data } = await apiInstance.getPhases(
    intersectionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **intersectionId** | [**number**] |  | defaults to undefined|


### Return type

**Array<PhaseDTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updatePhase**
> PhaseDTO updatePhase(phaseCreateRequest)


### Example

```typescript
import {
    PhaseControllerApi,
    Configuration,
    PhaseCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PhaseControllerApi(configuration);

let id: number; // (default to undefined)
let phaseCreateRequest: PhaseCreateRequest; //

const { status, data } = await apiInstance.updatePhase(
    id,
    phaseCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **phaseCreateRequest** | **PhaseCreateRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**PhaseDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

