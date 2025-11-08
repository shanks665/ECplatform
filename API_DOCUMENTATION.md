# API Documentation

## 目次
1. [認証API](#認証api)
2. [商品API](#商品api)
3. [カテゴリーAPI](#カテゴリーapi)
4. [ショッピングカートAPI](#ショッピングカートapi)
5. [注文API](#注文api)
6. [ユーザーAPI](#ユーザーapi)

---

## 認証API

### ユーザー登録
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "username": "newuser",
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "role": "CUSTOMER",
      "status": "PENDING"
    }
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### ログイン
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "admin",
  "password": "Admin@123"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@ecommerce.com",
      "role": "ADMIN"
    }
  }
}
```

---

## 商品API

### 商品一覧取得
```http
GET /api/products?page=0&size=20&sortBy=createdAt&sortDir=DESC
```

**クエリパラメータ:**
- `page`: ページ番号（デフォルト: 0）
- `size`: ページサイズ（デフォルト: 20）
- `sortBy`: ソート項目（デフォルト: createdAt）
- `sortDir`: ソート方向（ASC/DESC）

**レスポンス:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Laptop Pro 15",
        "slug": "laptop-pro-15",
        "sku": "LPT-001",
        "price": 1299.99,
        "salePrice": 1199.99,
        "stockQuantity": 50,
        "status": "ACTIVE",
        "averageRating": 4.5,
        "reviewCount": 25
      }
    ],
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

### 商品詳細取得
```http
GET /api/products/{id}
```

**レスポンス:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Laptop Pro 15",
    "slug": "laptop-pro-15",
    "sku": "LPT-001",
    "shortDescription": "High-performance laptop",
    "description": "Detailed description...",
    "price": 1299.99,
    "salePrice": 1199.99,
    "stockQuantity": 50,
    "status": "ACTIVE",
    "featured": true,
    "averageRating": 4.5,
    "reviewCount": 25,
    "categoryId": 1,
    "categoryName": "Electronics",
    "images": []
  }
}
```

### 商品検索
```http
GET /api/products/search?keyword=laptop&page=0&size=20
```

### 商品作成（管理者/セラー）
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "New Product",
  "sku": "PRD-001",
  "shortDescription": "Short description",
  "description": "Full description",
  "price": 99.99,
  "salePrice": 89.99,
  "stockQuantity": 100,
  "categoryId": 1,
  "brand": "BrandName"
}
```

---

## カテゴリーAPI

### カテゴリー一覧
```http
GET /api/categories
```

### ルートカテゴリー取得
```http
GET /api/categories/root
```

### カテゴリー詳細
```http
GET /api/categories/{id}
```

### 子カテゴリー取得
```http
GET /api/categories/{id}/children
```

---

## ショッピングカートAPI

### カート取得
```http
GET /api/cart
Authorization: Bearer {token}
```

**レスポンス:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userId": 2,
    "items": [
      {
        "id": 1,
        "quantity": 2,
        "unitPrice": 99.99,
        "totalPrice": 199.98,
        "productId": 1,
        "productName": "Product Name",
        "productInStock": true
      }
    ],
    "lastActivity": "2024-01-15T10:30:00"
  }
}
```

### カートに追加
```http
POST /api/cart/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

### カート商品更新
```http
PUT /api/cart/items/{itemId}?quantity=3
Authorization: Bearer {token}
```

### カートから削除
```http
DELETE /api/cart/items/{itemId}
Authorization: Bearer {token}
```

---

## 注文API

### 注文作成
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "shippingAddressId": 1,
  "billingAddressId": 1,
  "paymentMethod": "CREDIT_CARD",
  "notes": "Please deliver before 5 PM"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "orderNumber": "ORD-20240115103000-456",
    "orderDate": "2024-01-15T10:30:00",
    "status": "PENDING",
    "paymentStatus": "PENDING",
    "subtotal": 199.98,
    "taxAmount": 19.99,
    "shippingCost": 10.00,
    "totalAmount": 229.97,
    "items": [
      {
        "id": 1,
        "quantity": 2,
        "unitPrice": 99.99,
        "totalPrice": 199.98,
        "productName": "Product Name"
      }
    ]
  }
}
```

### 注文一覧取得
```http
GET /api/orders?page=0&size=10
Authorization: Bearer {token}
```

### 注文詳細
```http
GET /api/orders/{id}
Authorization: Bearer {token}
```

### 注文キャンセル
```http
PATCH /api/orders/{id}/cancel?reason=Changed my mind
Authorization: Bearer {token}
```

---

## ユーザーAPI

### 現在のユーザー情報取得
```http
GET /api/users/me
Authorization: Bearer {token}
```

### プロファイル更新
```http
PUT /api/users/me
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}
```

### パスワード変更
```http
PUT /api/users/me/password?newPassword=NewSecurePassword123
Authorization: Bearer {token}
```

---

## エラーレスポンス

すべてのエラーは以下の形式で返されます：

```json
{
  "success": false,
  "message": "Error message describing what went wrong",
  "timestamp": "2024-01-15T10:30:00"
}
```

### HTTPステータスコード

- `200 OK`: リクエスト成功
- `201 Created`: リソース作成成功
- `400 Bad Request`: 不正なリクエスト
- `401 Unauthorized`: 認証が必要
- `403 Forbidden`: アクセス権限なし
- `404 Not Found`: リソースが見つからない
- `500 Internal Server Error`: サーバーエラー

---

## 認証

ほとんどのエンドポイントでは、Bearerトークン認証が必要です。

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

トークンは `/api/auth/login` または `/api/auth/register` から取得できます。

---

## レート制限

現在、レート制限は実装されていませんが、本番環境では実装することを推奨します。

---

## バージョニング

現在のAPIバージョン: v1

将来のバージョンは `/api/v2/` のようにパスに含まれます。
