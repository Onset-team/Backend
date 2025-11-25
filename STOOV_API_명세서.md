# Onset Project API 명세서

이 문서는 Onset 프로젝트의 API 사양을 상세히 기술합니다.

---

## 1. User API

### 1.1. Google 소셜 로그인

- **POST** `/api/users/google`
- **설명:** Google에서 발급한 `credential`을 사용하여 사용자를 생성하거나 로그인합니다. 성공 시 사용자의 `UUID`가 세션에 저장됩니다.
- **인증:** 불필요

#### Parameters

**Request Body:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `credential` | `String` | Google OAuth 2.0 ID 토큰 | Y |

#### Request Example

```json
{
  "credential": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3..."
}
```

#### Response Example

**성공 (200 OK): 기존 유저 로그인**

```json
{
  "success": true,
  "data": {
    "isNewUser": false,
    "userId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
    "email": "testuser@gmail.com",
    "nickname": "테스트유저",
    "profileImageUrl": "https://lh3.googleusercontent.com/a/AATXAJz...",
    "accessToken": "ey...",
    "agreedToTerms": true
  },
  "error": null,
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**성공 (200 OK): 신규 유저 생성**

```json
{
  "success": true,
  "data": {
    "isNewUser": true,
    "userId": "b2c3d4e5-f6a7-8901-2345-67890abcdef1",
    "email": "newuser@gmail.com",
    "nickname": "새로운유저",
    "profileImageUrl": null,
    "accessToken": "ey...",
    "agreedToTerms": false
  },
  "error": null,
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (400 Bad Request): `credential` 누락 또는 형식 오류**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "C002",
    "message": "content: must not be blank"
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (503 Service Unavailable): 카카오/구글 API 통신 오류**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "S003",
    "message": "카카오 API 서비스가 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

---

### 1.2. 이용약관 동의

- **POST** `/api/users/agreements`
- **설명:** 현재 로그인된 사용자의 이용약관 동의 상태를 저장합니다.
- **인증:** 필요 (세션에 `userId` 존재해야 함)

#### Parameters
없음

#### Request Example
없음

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": null,
  "error": null,
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (404 Not Found): 존재하지 않는 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "U001",
    "message": "해당 사용자를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

---

### 1.3. 마이페이지 조회

- **GET** `/api/users/my`
- **설명:** 현재 로그인된 사용자의 마이페이지 정보를 조회합니다.
- **인증:** 필요 (세션에 `userId` 존재해야 함)

#### Parameters
없음

#### Request Example
없음

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": {
    "nickname": "테스트유저",
    "profileImageUrl": "https://my-bucket.s3.ap-northeast-2.amazonaws.com/...",
    "myReviewCount": 5,
    "myBookmarkCount": 12
  },
  "error": null,
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (404 Not Found): 존재하지 않는 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "U001",
    "message": "해당 사용자를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

---

### 1.4. 프로필 이미지 수정

- **PUT** `/api/users/my`
- **설명:** 현재 로그인된 사용자의 프로필 이미지를 수정합니다. `Content-Type`은 `multipart/form-data`여야 합니다.
- **인증:** 필요 (세션에 `userId` 존재해야 함)

#### Parameters

**Form Data:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `file` | `MultipartFile` | 업로드할 이미지 파일 | Y |

#### Request Example

```
(Postman 또는 cURL과 같은 도구를 사용하여 multipart/form-data 요청 전송)
```

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": {
    "nickname": "테스트유저",
    "profileImageUrl": "https://my-bucket.s3.ap-northeast-2.amazonaws.com/new-image...",
    "myReviewCount": 5,
    "myBookmarkCount": 12
  },
  "error": null,
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (400 Bad Request): 파일 누락**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "C004",
    "message": "필수 파라미터 누락: file"
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

---

### 1.5. 회원 탈퇴

- **DELETE** `/api/users`
- **설명:** 현재 로그인된 사용자를 탈퇴 처리합니다.
- **인증:** 필요 (세션에 `userId` 존재해야 함)

#### Parameters
없음

#### Request Example
없음

#### Response Example

**성공 (204 No Content)**
(응답 본문 없음)

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

**실패 (404 Not Found): 존재하지 않는 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "U001",
    "message": "해당 사용자를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T15:30:00.123"
}
```

---
## 2. Place API

### 2.1. 장소 상세 정보 조회

- **GET** `/api/places/{placeId}`
- **설명:** 특정 장소의 상세 정보를 조회합니다.
- **인증:** 선택 (로그인 시 북마크 여부 포함)

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `placeId` | `Long` | 장소의 고유 ID | Y |

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "여의도 한강공원",
    "address": "서울특별시 영등포구 여의동로 330",
    "district": "영등포구",
    "lat": 37.525,
    "lng": 126.934,
    "type": "RIVER_PARK",
    "contact": "02-1234-5678",
    "reservationUrl": "http://example.com/reserve",
    "operationTime": "24시간",
    "availableDays": "매일",
    "fee": "FREE",
    "maxPerformers": "UNLIMITED",
    "howToApply": "온라인 예약",
    "electricityAvailable": true,
    "thumbnailUrl": "https://my-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail.jpg",
    "isBookmarked": true,
    "reviewCount": 25
  },
  "error": null,
  "timestamp": "2023-11-23T16:00:00.000"
}
```

**실패 (404 Not Found): 존재하지 않는 장소**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "P001",
    "message": "해당 장소를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T16:00:00.000"
}
```

---

### 2.2. 장소 검색

- **GET** `/api/places/search`
- **설명:** 키워드를 사용하여 장소를 검색합니다. 페이지네이션을 지원합니다.
- **인증:** 선택 (로그인 시 북마크 여부 포함)

#### Parameters

**Query Parameters:**

| 이름 | 타입 | 설명 | 필수 | 기본값 |
| --- | --- | --- | --- | --- |
| `keyword` | `String` | 검색할 키워드 | Y | |
| `page` | `int` | 페이지 번호 (0부터 시작) | N | `0` |
| `size` | `int` | 한 페이지의 크기 | N | `10` |
| `sort` | `String` | 정렬 기준 (예: `name,asc`) | N | |
| `blockSize`| `int` | 페이지네이션 블록 크기 | N | `10` |

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": {
    "contents": [
      {
        "id": 1,
        "name": "여의도 한강공원",
        "address": "서울특별시 영등포구 여의동로 330",
        "lat": 37.525,
        "lng": 126.934,
        "isBookmarked": true
      }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 1,
    "isFirst": true,
    "isLast": true,
    "startPage": 1,
    "endPage": 1
  },
  "error": null,
  "timestamp": "2023-11-23T16:05:00.000"
}
```

**실패 (400 Bad Request): `keyword` 누락**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "C004",
    "message": "필수 파라미터 누락: keyword"
  },
  "timestamp": "2023-11-23T16:05:00.000"
}
```

---

### 2.3. 관심 장소 등록 (북마크)

- **POST** `/api/places/{placeId}/bookmarks`
- **설명:** 특정 장소를 관심 장소로 등록합니다.
- **인증:** 필요

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `placeId` | `Long` | 장소의 고유 ID | Y |

#### Response Example

**성공 (201 Created)**

```json
{
  "success": true,
  "data": null,
  "error": null,
  "timestamp": "2023-11-23T16:10:00.000"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T16:10:00.000"
}
```

**실패 (404 Not Found): 존재하지 않는 장소**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "P001",
    "message": "해당 장소를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T16:10:00.000"
}
```

**실패 (409 Conflict): 이미 북마크한 장소**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "B001",
    "message": "이미 북마크한 장소입니다."
  },
  "timestamp": "2023-11-23T16:10:00.000"
}
```

---

### 2.4. 관심 장소 해제 (북마크)

- **DELETE** `/api/places/{placeId}/bookmarks`
- **설명:** 특정 장소를 관심 장소에서 해제합니다.
- **인증:** 필요

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `placeId` | `Long` | 장소의 고유 ID | Y |

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": null,
  "error": null,
  "timestamp": "2023-11-23T16:15:00.000"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T16:15:00.000"
}
```

**실패 (404 Not Found): 북마크하지 않은 장소**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "B002",
    "message": "북마크하지 않은 장소입니다."
  },
  "timestamp": "2023-11-23T16:15:00.000"
}
```

---

### 2.5. 후기 작성

- **POST** `/api/places/{placeId}/reviews`
- **설명:** 특정 장소에 대한 후기를 작성합니다.
- **인증:** 필요

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `placeId` | `Long` | 장소의 고유 ID | Y |

**Request Body:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `content` | `String` | 후기 내용 (1000자 이내) | Y |

#### Request Example

```json
{
  "content": "공연하기 정말 좋은 곳이에요! 추천합니다."
}
```

#### Response Example

**성공 (201 Created)**

```json
{
  "success": true,
  "data": {
    "reviewId": 101,
    "content": "공연하기 정말 좋은 곳이에요! 추천합니다.",
    "createdAt": "2023-11-23T16:20:00.000Z"
  },
  "error": null,
  "timestamp": "2023-11-23T16:20:00.000"
}
```

**실패 (400 Bad Request): 내용 누락 또는 글자 수 초과**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "C002",
    "message": "content: must not be blank"
  },
  "timestamp": "2023-11-23T16:20:00.000"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T16:20:00.000"
}
```

---

### 2.6. 후기 목록 조회

- **GET** `/api/places/{placeId}/reviews`
- **설명:** 특정 장소의 후기 목록을 최신순으로 조회합니다.
- **인증:** 선택 (로그인 시 `isMyReview` 필드 포함)

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `placeId` | `Long` | 장소의 고유 ID | Y |

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": [
    {
      "reviewId": 102,
      "nickname": "다른유저***",
      "content": "주말에는 사람이 좀 많네요.",
      "isMyReview": false,
      "createdAt": "2023-11-22T11:00:00.000Z",
      "updatedAt": "2023-11-22T11:00:00.000Z"
    },
    {
      "reviewId": 101,
      "nickname": "테스트유***",
      "content": "공연하기 정말 좋은 곳이에요! 추천합니다.",
      "isMyReview": true,
      "createdAt": "2023-11-23T16:20:00.000Z",
      "updatedAt": "2023-11-23T16:20:00.000Z"
    }
  ],
  "error": null,
  "timestamp": "2023-11-23T16:25:00.000"
}
```

**실패 (404 Not Found): 존재하지 않는 장소**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "P001",
    "message": "해당 장소를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T16:25:00.000"
}
```

---

### 2.7. 전체 장소 목록 조회

- **GET** `/api/places`
- **설명:** 전체 장소 목록을 조회합니다.
- **인증:** 선택 (로그인 시 북마크 여부 포함)

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "여의도 한강공원",
      "address": "서울특별시 영등포구 여의동로 330",
      "lat": 37.525,
      "lng": 126.934,
      "isBookmarked": true
    },
    {
      "id": 2,
      "name": "반포 한강공원",
      "address": "서울특별시 서초구 신반포로11길 40",
      "lat": 37.512,
      "lng": 126.995,
      "isBookmarked": false
    }
  ],
  "error": null,
  "timestamp": "2023-11-23T16:30:00.000"
}
```
---
## 3. Review API

### 3.1. 후기 수정

- **PATCH** `/api/reviews/{reviewId}`
- **설명:** 특정 후기의 내용을 수정합니다.
- **인증:** 필요 (해당 후기 작성자만 가능)

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `reviewId` | `Long` | 후기의 고유 ID | Y |

**Request Body:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `content` | `String` | 수정할 후기 내용 | Y |

#### Request Example

```json
{
  "content": "주말에 사람 많은 것만 빼면 정말 좋아요."
}
```

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": {
    "reviewId": 101,
    "content": "주말에 사람 많은 것만 빼면 정말 좋아요.",
    "updatedAt": "2023-11-23T17:00:00.000Z"
  },
  "error": null,
  "timestamp": "2023-11-23T17:00:00.000"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T17:00:00.000"
}
```

**실패 (403 Forbidden): 수정 권한이 없는 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A002",
    "message": "권한이 없습니다."
  },
  "timestamp": "2023-11-23T17:00:00.000"
}
```

**실패 (404 Not Found): 존재하지 않는 후기**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "R001",
    "message": "해당 후기를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T17:00:00.000"
}
```

---

### 3.2. 후기 삭제

- **DELETE** `/api/reviews/{reviewId}`
- **설명:** 특정 후기를 삭제합니다.
- **인증:** 필요 (해당 후기 작성자만 가능)

#### Parameters

**Path Variable:**

| 이름 | 타입 | 설명 | 필수 |
| --- | --- | --- | --- |
| `reviewId` | `Long` | 후기의 고유 ID | Y |

#### Response Example

**성공 (200 OK)**

```json
{
  "success": true,
  "data": null,
  "error": null,
  "timestamp": "2023-11-23T17:05:00.000"
}
```

**실패 (401 Unauthorized): 로그인하지 않은 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 사용자입니다."
  },
  "timestamp": "2023-11-23T17:05:00.000"
}
```

**실패 (403 Forbidden): 삭제 권한이 없는 사용자**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A002",
    "message": "권한이 없습니다."
  },
  "timestamp": "2023-11-23T17:05:00.000"
}
```

**실패 (404 Not Found): 존재하지 않는 후기**

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "R001",
    "message": "해당 후기를 찾을 수 없습니다."
  },
  "timestamp": "2023-11-23T17:05:00.000"
}
```