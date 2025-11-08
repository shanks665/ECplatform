# セットアップガイド

## 環境要件

- Java 17以上
- Maven 3.6以上
- PostgreSQL 12以上（本番環境）

## 開発環境セットアップ

1. Javaのインストール確認
```bash
java -version
```

2. Mavenのインストール確認
```bash
mvn -version
```

3. アプリケーションのビルド
```bash
mvn clean install
```

4. アプリケーションの起動
```bash
mvn spring-boot:run
```

## 本番環境セットアップ

1. PostgreSQLデータベースの作成
2. 環境変数の設定
3. アプリケーションのビルドとデプロイ
