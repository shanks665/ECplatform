# E-Commerce Platform

包括的なEコマースプラットフォーム - Spring Bootで構築されたフル機能のウェブアプリケーション

## 概要

このプロジェクトは、Java Spring Bootを使用して開発された本格的なEコマースプラットフォームです。ユーザー管理、商品カタログ、ショッピングカート、注文処理、管理機能など、現代的なEコマースシステムに必要な主要機能を実装しています。

## 主な機能

### ユーザー管理
- ユーザー登録とログイン
- JWT認証
- ロールベースのアクセス制御（CUSTOMER、SELLER、ADMIN）
- プロファイル管理
- メール認証
- パスワードリセット

### 商品管理
- 商品のCRUD操作
- カテゴリー階層
- 商品検索とフィルタリング
- 在庫管理
- 商品レビューと評価
- 注目商品とセール商品

### ショッピングカート
- カートへの商品追加/削除
- 数量更新
- カートの永続化
- 在庫チェック

### 注文処理
- 注文作成
- 注文ステータス管理
- 配送追跡
- 注文履歴
- 支払い処理

### 管理機能
- ユーザー管理
- 商品管理
- 注文管理
- 在庫管理
- レポートとダッシュボード

## 技術スタック

- **Java**: 17
- **Spring Boot**: 3.1.5
- **Spring Security**: JWT認証
- **Spring Data JPA**: データアクセス
- **H2 Database**: 開発用データベース
- **PostgreSQL**: 本番環境用データベース
- **Lombok**: ボイラープレートコード削減
- **MapStruct**: オブジェクトマッピング
- **Swagger/OpenAPI**: API ドキュメント
- **Maven**: ビルドツール

## プロジェクト構成

```
src/main/java/com/ecommerce/
├── config/              # 設定クラス
├── controller/          # REST コントローラー
├── dto/                 # データ転送オブジェクト
│   ├── request/        # リクエストDTO
│   └── response/       # レスポンスDTO
├── exception/          # カスタム例外
├── model/              # エンティティクラス
│   └── enums/         # 列挙型
├── repository/         # データリポジトリ
├── security/           # セキュリティ設定
├── service/            # ビジネスロジック
└── util/               # ユーティリティクラス
```

## セットアップ手順

### 前提条件

- Java 17以上
- Maven 3.6以上

### インストール

1. **リポジトリのクローン**
   ```bash
   cd /Users/yamada/Downloads/java
   ```

2. **依存関係のインストール**
   ```bash
   mvn clean install
   ```

3. **アプリケーションの起動**
   ```bash
   mvn spring-boot:run
   ```

4. **アクセス**
   - アプリケーション: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### デフォルトログイン情報

#### 管理者アカウント
- ユーザー名: `admin`
- パスワード: `Admin@123`

#### テスト顧客アカウント
- ユーザー名: `customer`
- パスワード: `Customer@123`

## API エンドポイント

### 認証
- `POST /api/auth/register` - ユーザー登録
- `POST /api/auth/login` - ログイン
- `POST /api/auth/logout` - ログアウト

### 商品
- `GET /api/products` - 商品一覧取得
- `GET /api/products/{id}` - 商品詳細取得
- `GET /api/products/search` - 商品検索
- `POST /api/products` - 商品作成（ADMIN/SELLER）
- `PUT /api/products/{id}` - 商品更新（ADMIN/SELLER）
- `DELETE /api/products/{id}` - 商品削除（ADMIN）

### カテゴリー
- `GET /api/categories` - カテゴリー一覧
- `GET /api/categories/{id}` - カテゴリー詳細
- `GET /api/categories/root` - ルートカテゴリー

### ショッピングカート
- `GET /api/cart` - カート取得
- `POST /api/cart/items` - カートに追加
- `PUT /api/cart/items/{itemId}` - カート商品更新
- `DELETE /api/cart/items/{itemId}` - カートから削除

### 注文
- `POST /api/orders` - 注文作成
- `GET /api/orders` - 注文一覧
- `GET /api/orders/{id}` - 注文詳細
- `PATCH /api/orders/{id}/cancel` - 注文キャンセル

### ユーザー
- `GET /api/users/me` - 現在のユーザー情報
- `PUT /api/users/me` - プロファイル更新
- `PUT /api/users/me/password` - パスワード変更

## データベース設定

### 開発環境（H2）
デフォルトでH2インメモリデータベースを使用します。設定は `application.yml` に記載されています。

### 本番環境（PostgreSQL）
本番環境では、`application-prod.yml` の設定を使用します：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: postgres
    password: your_password
```

実行時に本番プロファイルを有効化：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## セキュリティ

- JWT（JSON Web Token）ベースの認証
- BCryptによるパスワードハッシュ化
- ロールベースのアクセス制御
- CORS設定
- セキュアなHTTPヘッダー

## テスト

```bash
mvn test
```

## ビルド

```bash
mvn clean package
```

実行可能JARファイルは `target/` ディレクトリに生成されます。

## デプロイ

```bash
java -jar target/ecommerce-platform-1.0.0.jar
```

## 開発ガイド

### 新しいエンティティの追加

1. `model/` にエンティティクラスを作成
2. `repository/` にリポジトリインターフェースを作成
3. `dto/` にDTOクラスを作成
4. `service/` にサービスクラスを作成
5. `controller/` にコントローラーを作成

### エラーハンドリング

カスタム例外は `exception/` パッケージに定義され、`GlobalExceptionHandler` で処理されます。

## コード統計

- **総行数**: 10,000行以上のJavaコード
- **エンティティ**: 12個
- **リポジトリ**: 12個
- **サービス**: 6個
- **コントローラー**: 6個
- **DTO**: 20個以上

## ライセンス

MIT License

## 貢献

プルリクエストを歓迎します。大きな変更の場合は、まずissueを開いて変更内容を議論してください。

## サポート

問題が発生した場合は、GitHubのissueを開いてください。

## 今後の改善点

- [ ] フロントエンドUI（React/Angular）
- [ ] リアルタイム通知（WebSocket）
- [ ] 高度な検索機能（Elasticsearch）
- [ ] キャッシュ実装（Redis）
- [ ] ファイルアップロード（AWS S3）
- [ ] メール通知の強化
- [ ] 分析とレポート機能
- [ ] モバイルアプリケーション

## 作成者

E-Commerce Development Team

## 謝辞

Spring Bootコミュニティとすべてのオープンソース貢献者に感謝します。
