
# 설정
EC2_USER="ec2-user"
EC2_HOST="15.164.52.83"
JAR_NAME="haeyum-0.0.1-SNAPSHOT.jar"
LOCAL_JAR_PATH="/Users/sanggyun/Documents/GitHub/haeyum-server/build/libs/$JAR_NAME"
IMAGE_PATH="/Users/sanggyun/Documents/GitHub/haeyum-server/src/main/resources/img/defaultImgOfNews.jpg"
TARGET_DIR="/home/ec2-user/haeyum/"
TARGET_IMG_DIR="/home/ec2-user/haeyum/image/"
PEM_FILE="/Users/sanggyun/Downloads/haeyum-server-keyPair.pem"

echo ">>> 1. 빌드 시작 <<<"
./gradlew clean build -x test


echo ">>> 2. JAR 파일 전송"
scp -i "$PEM_FILE" "$LOCAL_JAR_PATH" $EC2_USER@$EC2_HOST:$TARGET_DIR
#scp -i "$PEM_FILE" "$IMAGE_PATH" $EC2_USER@$EC2_HOST:$TARGET_IMG_DIR


#echo ">>> 3. EC2에서 실행"
#ssh -i "$PEM_FILE" "$EC2_USER@$EC2_HOST" "pkill -f $JAR_NAME; java -jar $TARGET_DIR$JAR_NAME --spring.prifiles.active=local"

echo ">>> 4. 배포 완료"
