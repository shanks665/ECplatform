# 🛒 E-Commerce Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Code Size](https://img.shields.io/badge/Code-10000%2B%20lines-blue?style=for-the-badge)

**包括的なEコマースプラットフォーム - Spring Bootで構築されたフル機能のウェブアプリケーション**

[機能](#主な機能) •
[インストール](#インストール手順) •
[使い方](#使用方法) •
[API](#api-ドキュメント) •
[貢献](#貢献)

</div>

---

## 📋 目次

- [概要](#概要)
- [主な機能](#主な機能)
- [技術スタック](#技術スタック)
- [必要な環境](#必要な環境)
- [インストール手順](#インストール手順)
- [使用方法](#使用方法)
- [API ドキュメント](#api-ドキュメント)
- [プロジェクト構成](#プロジェクト構成)
- [データベース設定](#データベース設定)
- [トラブルシューティング](#トラブルシューティング)
- [FAQ](#よくある質問)
- [貢献](#貢献)
- [ライセンス](#ライセンス)

---

## 🎯 概要

このプロジェクトは、**Java 17** と **Spring Boot 3.1.5** を使用して開発された、本格的なEコマースプラットフォームです。

### ✨ プロジェクトの特徴

- ✅ **10,000行以上**の高品質なJavaコード
- ✅ **フロントエンドUI完備** - 美しいレスポンシブWebページ
- ✅ **RESTful API** 完全実装
- ✅ **JWT認証**とロールベースアクセス制御
- ✅ **Swagger/OpenAPI** による完全なAPIドキュメント
- ✅ **H2** と **PostgreSQL** のデータベースサポート
- ✅ **即座に使える**サンプルデータ付き
- ✅ **本番環境対応**の設計

### 🖼️ スクリーンショット

アプリケーションを起動後、以下のURLでアクセスできます：

- **ホームページ**: `http://localhost:8080/`
- **商品一覧**: カテゴリー別、検索、フィルター機能
- **ショッピングカート**: リアルタイム更新
- **ログインページ**: デモアカウント対応

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

### バックエンド
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

### フロントエンド
- **HTML5**: セマンティックマークアップ
- **CSS3**: モダンなスタイリング、レスポンシブデザイン
- **JavaScript (ES6+)**: 非同期API通信、DOM操作
- **Fetch API**: REST API呼び出し
- **LocalStorage**: カート・認証情報の永続化

## プロジェクト構成

```
src/
├── main/
│   ├── java/com/ecommerce/
│   │   ├── config/              # 設定クラス
│   │   ├── controller/          # REST コントローラー
│   │   ├── dto/                 # データ転送オブジェクト
│   │   │   ├── request/        # リクエストDTO
│   │   │   └── response/       # レスポンスDTO
│   │   ├── exception/          # カスタム例外
│   │   ├── model/              # エンティティクラス
│   │   │   └── enums/         # 列挙型
│   │   ├── repository/         # データリポジトリ
│   │   ├── security/           # セキュリティ設定
│   │   ├── service/            # ビジネスロジック
│   │   └── util/               # ユーティリティクラス
│   └── resources/
│       ├── static/             # フロントエンド
│       │   ├── index.html      # ホームページ
│       │   ├── login.html      # ログインページ
│       │   ├── cart.html       # カートページ
│       │   ├── css/           # スタイルシート
│       │   └── js/            # JavaScript
│       ├── application.yml     # アプリケーション設定
│       └── application-prod.yml # 本番環境設定
```

## 💻 必要な環境

### 📦 必須ソフトウェア

| ソフトウェア | バージョン | ダウンロード |
|------------|-----------|------------|
| **Java JDK** | 17以上 | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java17) / [OpenJDK](https://jdk.java.net/17/) |
| **Maven** | 3.6以上 | [Apache Maven](https://maven.apache.org/download.cgi) |
| **Git** | 最新版 | [Git](https://git-scm.com/downloads) |

### 🔧 推奨ツール

| ツール | 用途 | ダウンロード |
|-------|------|------------|
| **IntelliJ IDEA** | IDE | [JetBrains](https://www.jetbrains.com/idea/download/) |
| **VS Code** | エディタ | [Microsoft](https://code.visualstudio.com/) |
| **Postman** | API テスト | [Postman](https://www.postman.com/downloads/) |
| **PostgreSQL** | 本番DB | [PostgreSQL](https://www.postgresql.org/download/) |

---

## 🚀 インストール手順

### Step 1: Javaのインストール

#### macOS (Homebrew使用)
```bash
# Homebrewのインストール（未インストールの場合）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Java 17のインストール
brew install openjdk@17

# パスの設定
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# 確認
java -version
```

#### Windows
1. [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)をダウンロード
2. インストーラーを実行
3. 環境変数`JAVA_HOME`を設定
4. `Path`に`%JAVA_HOME%\bin`を追加

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

### Step 2: Mavenのインストール

#### macOS
```bash
brew install maven
mvn -version
```

#### Windows
1. [Maven](https://maven.apache.org/download.cgi)をダウンロード
2. 解凍して適当な場所に配置
3. 環境変数`M2_HOME`を設定
4. `Path`に`%M2_HOME%\bin`を追加

#### Linux
```bash
sudo apt install maven
mvn -version
```

### Step 3: プロジェクトのクローン

```bash
# GitHubからクローン
git clone https://github.com/shanks665/ECplatform.git
cd ECplatform
```

**または** ZIPファイルをダウンロード：
- [Download ZIP](https://github.com/shanks665/ECplatform/archive/refs/heads/main.zip)

### Step 4: ビルドと起動

```bash
# 依存関係のダウンロードとビルド
mvn clean install -DskipTests

# アプリケーションの起動
mvn spring-boot:run
```

**Java 17を明示的に指定する場合：**
```bash
export JAVA_HOME=/path/to/java-17
mvn spring-boot:run
```

### Step 5: アクセス

起動が成功すると、以下のメッセージが表示されます：

```
==============================================
E-Commerce Platform Started Successfully!
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console
==============================================
```

ブラウザで以下のURLにアクセス：

| サービス | URL | 説明 |
|---------|-----|------|
| **🏠 ホームページ** | http://localhost:8080/ | **メインアプリケーション（フロントエンド）** |
| **🛒 ショッピング** | http://localhost:8080/index.html | 商品一覧・検索 |
| **🔐 ログイン** | http://localhost:8080/login.html | ユーザーログイン |
| **🛒 カート** | http://localhost:8080/cart.html | ショッピングカート |
| **📚 Swagger UI** | http://localhost:8080/swagger-ui.html | API ドキュメント・テスト |
| **🗄️ H2 Console** | http://localhost:8080/h2-console | データベース管理 |
| **⚡ REST API** | http://localhost:8080/api | APIエンドポイント |

---

## 🎮 使用方法

### 🌐 フロントエンドの使い方

#### 1. ホームページにアクセス
```
http://localhost:8080/
```

#### 2. 商品を閲覧
- **カテゴリーで絞り込み**: Electronics、Clothing、Books等
- **検索**: 上部の検索ボックスから商品を検索
- **フィルター**: すべて/注目商品/セール中

#### 3. カートに追加
- 商品カードの「🛒 カートに追加」ボタンをクリック
- 右上のカートアイコンから確認

#### 4. ログイン
- 右上の「ログイン」をクリック
- デモアカウントを使用（下記参照）

### デフォルトログイン情報

| 役割 | ユーザー名 | パスワード | 権限 |
|-----|-----------|----------|------|
| **管理者** | `admin` | `Admin@123` | 全機能アクセス |
| **顧客** | `customer` | `Customer@123` | 一般ユーザー機能 |

### H2 Database Console ログイン

| 項目 | 値 |
|-----|---|
| **JDBC URL** | `jdbc:h2:mem:ecommerce` |
| **Username** | `sa` |
| **Password** | （空白） |

### 初期データ

アプリケーション起動時に、以下のデータが自動的に作成されます：

- 👤 **ユーザー**: 2名（admin, customer）
- 📁 **カテゴリー**: 5カテゴリー
- 📦 **商品**: 11商品

---

## 📚 API ドキュメント

### Swagger UI の使用

1. ブラウザで http://localhost:8080/swagger-ui.html にアクセス
2. 各エンドポイントをクリックして詳細を確認
3. 「Try it out」をクリックしてAPIをテスト

### 🔐 認証が必要なエンドポイントの使用方法

1. **ログインしてトークンを取得：**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "Admin@123"
  }'
```

2. **レスポンスからトークンをコピー：**

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIml...",
    "tokenType": "Bearer"
  }
}
```

3. **認証が必要なAPIを呼び出す：**

```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 📋 主要なAPI エンドポイント

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

## 🗄️ データベース設定

### 開発環境（H2 Database）

デフォルトでH2インメモリデータベースを使用します。アプリケーション起動時に自動的にテーブルとサンプルデータが作成されます。

**設定ファイル：** `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:ecommerce
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
```

### 本番環境（PostgreSQL）

**設定ファイル：** `src/main/resources/application-prod.yml`

1. **PostgreSQLのインストール：**
```bash
# macOS
brew install postgresql
brew services start postgresql

# Ubuntu/Debian
sudo apt install postgresql
sudo systemctl start postgresql
```

2. **データベースの作成：**
```sql
CREATE DATABASE ecommerce;
CREATE USER ecommerce_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE ecommerce TO ecommerce_user;
```

3. **環境変数の設定：**
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=ecommerce
export DB_USERNAME=ecommerce_user
export DB_PASSWORD=your_password
```

4. **本番プロファイルで起動：**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 🔒 セキュリティ

### 実装されているセキュリティ機能

| 機能 | 説明 |
|-----|------|
| **JWT認証** | JSON Web Tokenによるステートレス認証 |
| **BCrypt** | パスワードのハッシュ化（10ラウンド） |
| **RBAC** | ロールベースのアクセス制御 |
| **CORS** | クロスオリジンリソース共有の設定 |
| **XSS対策** | HTTPヘッダーによる保護 |
| **CSRF対策** | クロスサイトリクエストフォージェリ対策 |

### JWTトークンの設定

デフォルトのシークレットキーは開発用です。本番環境では必ず変更してください。

**環境変数で設定：**
```bash
export JWT_SECRET=your-256-bit-secret-key-here
export JWT_EXPIRATION=86400000  # 24時間（ミリ秒）
```

## 🧪 テスト

```bash
# 全テストの実行
mvn test

# 特定のテストクラスを実行
mvn test -Dtest=UserServiceTest

# カバレッジレポート生成
mvn clean test jacoco:report
```

## 📦 ビルド

### 開発用ビルド
```bash
mvn clean package
```

### 本番用ビルド（最適化）
```bash
mvn clean package -Pprod -DskipTests
```

実行可能JARファイルは `target/` ディレクトリに生成されます。

## 🚢 デプロイ

### ローカル実行
```bash
java -jar target/ecommerce-platform-1.0.0.jar
```

### 本番環境で実行
```bash
java -jar target/ecommerce-platform-1.0.0.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### Dockerでの実行
```bash
# Dockerイメージのビルド
docker build -t ecommerce-platform .

# コンテナの起動
docker run -p 8080:8080 ecommerce-platform
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

---

## 🔧 トラブルシューティング

### よくある問題と解決方法

#### 1. ポート8080が既に使用されている

**エラー:**
```
Web server failed to start. Port 8080 was already in use.
```

**解決方法:**
```bash
# macOS/Linux: ポート8080を使用しているプロセスを確認
lsof -i :8080

# プロセスを終了
kill -9 <PID>

# または、別のポートで起動
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### 2. Java バージョンの不一致

**エラー:**
```
java.lang.UnsupportedClassVersionError
```

**解決方法:**
```bash
# Java 17が使用されているか確認
java -version

# JAVA_HOMEを明示的に設定
export JAVA_HOME=/path/to/java-17
mvn clean install
```

#### 3. Maven ビルドエラー

**エラー:**
```
Failed to execute goal ... compilation failure
```

**解決方法:**
```bash
# キャッシュをクリア
mvn clean

# 依存関係を再ダウンロード
mvn dependency:purge-local-repository

# 再ビルド
mvn clean install -U
```

#### 4. H2 Console にアクセスできない

**確認事項:**
- アプリケーションが起動しているか確認
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ecommerce`
- Username: `sa`
- Password: （空白）

#### 5. Lombok が動作しない

**解決方法（IntelliJ IDEA）:**
1. Settings → Plugins → "Lombok" をインストール
2. Settings → Build → Compiler → Annotation Processors
3. "Enable annotation processing" をチェック
4. プロジェクトを再ビルド

**解決方法（VS Code）:**
1. Extension "Language Support for Java" をインストール
2. 設定で `java.jdt.ls.lombokSupport.enabled` を `true` に設定

---

## ❓ よくある質問（FAQ）

<details>
<summary><b>Q1: このプロジェクトを商用利用できますか？</b></summary>

A: はい、MIT Licenseなので商用利用可能です。ただし、ライセンス表記は保持してください。
</details>

<details>
<summary><b>Q2: データベースをMySQLに変更できますか？</b></summary>

A: はい、可能です。以下の手順で変更できます：

1. `pom.xml` にMySQL依存関係を追加：
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. `application.yml` のデータソース設定を変更：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```
</details>

<details>
<summary><b>Q3: フロントエンドは含まれていますか？</b></summary>

A: このプロジェクトはバックエンドAPIのみです。フロントエンドはReact、Angular、Vue.jsなどで別途構築できます。Swagger UIでAPIをテストできます。
</details>

<details>
<summary><b>Q4: 本番環境にデプロイする方法は？</b></summary>

A: いくつかの方法があります：

1. **JARファイルとして実行:**
```bash
java -jar target/ecommerce-platform-1.0.0.jar --spring.profiles.active=prod
```

2. **Docker で実行:**
```bash
docker build -t ecommerce .
docker run -p 8080:8080 ecommerce
```

3. **クラウドサービス（AWS, Azure, GCP）にデプロイ**
</details>

<details>
<summary><b>Q5: メール送信機能は動作しますか？</b></summary>

A: メール送信のコードは実装されていますが、SMTPサーバーの設定が必要です。`application.yml` で以下を設定してください：

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```
</details>

<details>
<summary><b>Q6: APIのレート制限はありますか？</b></summary>

A: 現在、レート制限は実装されていません。本番環境では、Spring Cloud Gateway や Nginx を使用してレート制限を追加することをお勧めします。
</details>

<details>
<summary><b>Q7: HTTPS対応はできますか？</b></summary>

A: はい、SSL証明書を設定することで可能です：

```yaml
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: your_password
    key-store-type: PKCS12
```
</details>

---

## 📊 コード統計

### バックエンド (Java)
| 項目 | 数量 |
|-----|------|
| **総コード行数** | 10,000+ 行 |
| **Javaファイル** | 85+ ファイル |
| **エンティティ** | 12 個 |
| **リポジトリ** | 12 個 |
| **サービス** | 6 個 |
| **コントローラー** | 6 個 |
| **DTO** | 20+ 個 |
| **列挙型** | 6 個 |

### フロントエンド
| 項目 | 数量 |
|-----|------|
| **HTMLページ** | 3 ページ |
| **CSS** | 500+ 行 |
| **JavaScript** | 400+ 行 |
| **機能** | 商品表示、検索、カート、ログイン |

---

## 📄 ライセンス

このプロジェクトは **MIT License** の下でライセンスされています。

詳細は [LICENSE](LICENSE) ファイルを参照してください。

---

## 🤝 貢献

プルリクエストを歓迎します！

### 貢献ガイドライン

1. このリポジトリをフォーク
2. 機能ブランチを作成 (`git checkout -b feature/AmazingFeature`)
3. 変更をコミット (`git commit -m 'Add some AmazingFeature'`)
4. ブランチにプッシュ (`git push origin feature/AmazingFeature`)
5. プルリクエストを作成

### コードスタイル

- Javaコーディング規約に従ってください
- Lombokアノテーションを活用
- 適切なJavaDocコメントを追加
- テストコードを書く

---

## 📞 サポート

問題が発生した場合は、以下の方法でサポートを受けられます：

- 🐛 [Issue Tracker](https://github.com/shanks665/ECplatform/issues) でバグを報告
- 💡 [Discussions](https://github.com/shanks665/ECplatform/discussions) で質問
- 📧 メール: support@ecommerce.com

---

## 🗺️ ロードマップ

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
