# IntersectionControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createIntersection**](#createintersection) | **POST** /intersections | |
|[**deleteIntersection**](#deleteintersection) | **DELETE** /intersections/{id} | |
|[**getIntersection**](#getintersection) | **GET** /intersections/{id} | |
|[**getIntersectionList**](#getintersectionlist) | **GET** /intersections/list | |
|[**updateIntersection**](#updateintersection) | **PUT** /intersections/{id} | |

# **createIntersection**
> IntersectionDTO createIntersection(intersectionCreateRequest)


### Example

```typescript
import {
    IntersectionControllerApi,
    Configuration,
    IntersectionCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new IntersectionControllerApi(configuration);

let intersectionCreateRequest: IntersectionCreateRequest; //

const { status, data } = await apiInstance.createIntersection(
    intersectionCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **intersectionCreateRequest** | **IntersectionCreateRequest**|  | |


### Return type

**IntersectionDTO**

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

# **deleteIntersection**
> deleteIntersection()


### Example

```typescript
import {
    IntersectionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new IntersectionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deleteIntersection(
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

# **getIntersection**
> IntersectionDTO getIntersection()


### Example

```typescript
import {
    IntersectionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new IntersectionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getIntersection(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**IntersectionDTO**

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

# **getIntersectionList**
> Array<IntersectionDTO> getIntersectionList()


### Example

```typescript
import {
    IntersectionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new IntersectionControllerApi(configuration);

const { status, data } = await apiInstance.getIntersectionList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<IntersectionDTO>**

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

# **updateIntersection**
> IntersectionDTO updateIntersection(intersectionUpdateRequest)


### Example

```typescript
import {
    IntersectionControllerApi,
    Configuration,
    IntersectionUpdateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new IntersectionControllerApi(configuration);

let id: number; // (default to undefined)
let intersectionUpdateRequest: IntersectionUpdateRequest; //

const { status, data } = await apiInstance.updateIntersection(
    id,
    intersectionUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **intersectionUpdateRequest** | **IntersectionUpdateRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**IntersectionDTO**

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

