# UStar-Backend

## MySQL Docker 시작 절차

---

### MySQL docker 생성

```bash
docker run --name 컨테이너이름 -e MYSQL_ROOT_PASSWORD=ROOT비밀번호 -e MYSQL_DATABASE=DB이름 -e MYSQL_USER=사용자이름 -e MYSQL_PASSWORD=사용자비밀번호 -p 3306:3306 -d mysql:8
```

### Docker 컨테이너 접속

```bash
docker exec -it 컨테이너이름 bash
```

1. MySQL 접속 및 Table 확인

```bash
mysql -u 사용자이름 -p
```

`-u` 다음에는 사용자 이름을 넣고 `-p` 옵션 후 엔터를 치면 비밀번호 입력칸이 나온다.

인증이 성공하면 프롬프트가 `mysql>` 로 바뀐다.

2. DB 전환

```bash
use DB이름;
```

3. table 확인

```bash
show tables;
```
결과는 다음과 같을 것이다.
```
+-----------------+
| Tables_in_ustar |
+-----------------+
| DB이름           |
+-----------------+
```
