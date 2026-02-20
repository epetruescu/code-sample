# SignalGroupControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createSignalGroup**](#createsignalgroup) | **POST** /signal-groups | |
|[**deleteSignalGroup**](#deletesignalgroup) | **DELETE** /signal-groups/{id} | |
|[**getSignalGroup**](#getsignalgroup) | **GET** /signal-groups/{id} | |
|[**getSignalGroups**](#getsignalgroups) | **GET** /signal-groups | |
|[**updateSignalGroup**](#updatesignalgroup) | **PUT** /signal-groups/{id} | |

# **createSignalGroup**
> SignalGroupDTO createSignalGroup(signalGroupCreateRequest)


### Example

```typescript
import {
    SignalGroupControllerApi,
    Configuration,
    SignalGroupCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SignalGroupControllerApi(configuration);

let signalGroupCreateRequest: SignalGroupCreateRequest; //

const { status, data } = await apiInstance.createSignalGroup(
    signalGroupCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **signalGroupCreateRequest** | **SignalGroupCreateRequest**|  | |


### Return type

**SignalGroupDTO**

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

# **deleteSignalGroup**
> deleteSignalGroup()


### Example

```typescript
import {
    SignalGroupControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SignalGroupControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deleteSignalGroup(
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

# **getSignalGroup**
> SignalGroupDTO getSignalGroup()


### Example

```typescript
import {
    SignalGroupControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SignalGroupControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getSignalGroup(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**SignalGroupDTO**

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

# **getSignalGroups**
> Array<SignalGroupDTO> getSignalGroups()


### Example

```typescript
import {
    SignalGroupControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SignalGroupControllerApi(configuration);

let intersectionId: number; // (default to undefined)

const { status, data } = await apiInstance.getSignalGroups(
    intersectionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **intersectionId** | [**number**] |  | defaults to undefined|


### Return type

**Array<SignalGroupDTO>**

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

# **updateSignalGroup**
> SignalGroupDTO updateSignalGroup(signalGroupDTO)


### Example

```typescript
import {
    SignalGroupControllerApi,
    Configuration,
    SignalGroupDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new SignalGroupControllerApi(configuration);

let id: number; // (default to undefined)
let signalGroupDTO: SignalGroupDTO; //

const { status, data } = await apiInstance.updateSignalGroup(
    id,
    signalGroupDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **signalGroupDTO** | **SignalGroupDTO**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**SignalGroupDTO**

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

