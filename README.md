# Job-Hunt-App（内定ホイホイ）

応募先（企業）を登録し、選考ステータス・面接日・メモ・履歴を管理するWebアプリです。

- 公開URL: `https://job-hunt-app-2qwq.onrender.com`

## 主な機能

- 応募先の一覧表示
- 応募先の新規登録 / 編集 / 削除
- ステータス管理（応募前 / 書類選考中 / 面接… / 内定 / お見送り / 辞退）
- 過去履歴の追加・閲覧
- 旧URL互換（`/applications/new` → `/applications/create` に自動遷移）

## 画面（URL）

- 一覧: `/applications`
- 新規登録: `/applications/create`
- 詳細: `/applications/{id}`
- 編集: `/applications/edit/{id}`

## 技術スタック

- Java 17
- Spring Boot 3.2.x
- Thymeleaf
- MongoDB（MongoDB Atlas）
- Render（Dockerでデプロイ）

## 環境変数（MongoDB）

MongoDB接続URIを以下のどちらかで設定してください（どちらか1つでOK）。

- `SPRING_DATA_MONGODB_URI`（推奨）
- `MONGODB_URI`

※ URIの中にユーザー名/パスワード等の機密情報が含まれるため、READMEやGitHubに値は載せないでください。

### Render（本番）

Renderの Web Service → `Environment` で上記KEYを追加して設定します。

### ローカル（例）

ターミナルで環境変数を設定して起動します（値は自分のものに置き換え）。

```bash
export SPRING_DATA_MONGODB_URI="YOUR_MONGODB_URI"
./mvnw spring-boot:run
```

## ローカル起動

前提:

- Java 17

起動:

```bash
./mvnw spring-boot:run
```

アクセス:

- `http://localhost:8081/applications`

## テスト

```bash
./mvnw test
```

## 実装上の工夫

- 削除はGETで副作用を起こさないようにし、POSTでのみ削除する設計に変更（リンクを踏んだだけで削除される事故を防止）
- 旧URL（`/applications/new`）にアクセスされても新規登録画面へ遷移するよう互換ルートを用意

