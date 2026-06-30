#!/bin/bash
#===============================================================================
# learn-system 服务启停脚本
#
# 用法: ./start.sh {start|stop|restart|status}
#
# 示例:
#   ./start.sh start      # 启动服务
#   ./start.sh stop       # 停止服务
#   ./start.sh restart    # 重启服务
#   ./start.sh status     # 查看运行状态
#
# 环境变量（可选）:
#   JAVA_OPTS   JVM 参数（默认: -Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError）
#   JAVA_HOME   JDK 安装路径
#===============================================================================

set -e

# --- 配置 ---
APP_NAME="learn-system"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAR_FILE=$(ls "${SCRIPT_DIR}"/yub-edu-biz-*.jar 2>/dev/null | head -1)
CONFIG_DIR="${SCRIPT_DIR}/config"
PID_FILE="${SCRIPT_DIR}/app.pid"
LOG_DIR="${SCRIPT_DIR}/logs"
LOG4J2_CONFIG="${CONFIG_DIR}/log4j2.xml"

# JVM 参数（可通过环境变量覆盖）
JAVA_OPTS="${JAVA_OPTS:--Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError}"

# Java 可执行文件
JAVA_CMD="java"
if [ -n "${JAVA_HOME}" ]; then
    JAVA_CMD="${JAVA_HOME}/bin/java"
fi

# Spring Boot 参数
SPRING_OPTS="--spring.profiles.active=prod --spring.config.additional-location=./config/"
# Log4j2 外部配置文件
LOG_OPTS="-Dlog4j2.configurationFile=${LOG4J2_CONFIG}"

# --- 函数 ---
usage() {
    echo "用法: $0 {start|stop|restart|status}"
    echo ""
    echo "  start      启动服务"
    echo "  stop       停止服务"
    echo "  restart    重启服务"
    echo "  status     查看运行状态"
    exit 1
}

get_pid() {
    if [ -f "${PID_FILE}" ]; then
        cat "${PID_FILE}"
    fi
}

is_running() {
    local pid
    pid=$(get_pid)
    if [ -n "${pid}" ] && kill -0 "${pid}" 2>/dev/null; then
        return 0
    fi
    return 1
}

start() {
    if [ -z "${JAR_FILE}" ]; then
        echo "[${APP_NAME}] 错误: 未找到 JAR 文件 (yub-edu-biz-*.jar)"
        exit 1
    fi

    if is_running; then
        echo "[${APP_NAME}] 服务已在运行中, PID: $(get_pid)"
        return 0
    fi

    # 创建日志目录
    mkdir -p "${LOG_DIR}"

    echo -n "[${APP_NAME}] 正在启动..."
    nohup ${JAVA_CMD} ${JAVA_OPTS} ${LOG_OPTS} -jar "${JAR_FILE}" ${SPRING_OPTS} \
        > "${LOG_DIR}"/nohup.log 2>&1 &
    local pid=$!
    echo "${pid}" > "${PID_FILE}"

    # 等待启动完成（最多 30 秒）
    local count=0
    while [ ${count} -lt 30 ]; do
        if is_running; then
            echo " 成功 (PID: ${pid})"
            return 0
        fi
        sleep 1
        count=$((count + 1))
    done

    # 超时：检查进程是否已异常退出
    if ! kill -0 "${pid}" 2>/dev/null; then
        echo " 失败，请查看日志: ${LOG_DIR}/nohup.log"
        rm -f "${PID_FILE}"
        exit 1
    fi
    echo " 已启动 (PID: ${pid}, 等待端口就绪中)"
}

stop() {
    local pid
    pid=$(get_pid)

    if [ -z "${pid}" ]; then
        echo "[${APP_NAME}] 服务未运行"
        rm -f "${PID_FILE}"
        return 0
    fi

    if ! kill -0 "${pid}" 2>/dev/null; then
        echo "[${APP_NAME}] 进程 ${pid} 不存在，清理 PID 文件"
        rm -f "${PID_FILE}"
        return 0
    fi

    echo -n "[${APP_NAME}] 正在停止 (PID: ${pid})..."

    # 发送 SIGTERM 优雅关闭
    kill "${pid}"
    local count=0
    while [ ${count} -lt 30 ]; do
        if ! kill -0 "${pid}" 2>/dev/null; then
            echo " 已停止"
            rm -f "${PID_FILE}"
            return 0
        fi
        sleep 1
        count=$((count + 1))
    done

    # 超时，强制终止
    echo -n " 等待超时，强制终止..."
    kill -9 "${pid}" 2>/dev/null || true
    sleep 1
    rm -f "${PID_FILE}"
    echo " 已强制停止"
}

restart() {
    stop
    sleep 2
    start
}

status() {
    if is_running; then
        local pid
        pid=$(get_pid)
        echo "[${APP_NAME}] 运行中 (PID: ${pid})"
        return 0
    else
        echo "[${APP_NAME}] 未运行"
        return 1
    fi
}

# --- 主流程 ---
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        usage
        ;;
esac
