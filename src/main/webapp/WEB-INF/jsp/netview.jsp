<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 17-9-16
  Time: 下午2:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>交换机监控列表</title>
    <script type="text/javascript" src="/netview/js/jquery.js"></script>
    <script type="text/javascript" src="/netview/js/netview.js"></script>
    <link rel="stylesheet" type="text/css" href="/netview/css/netview.css">
    <script type="text/javascript">
        setInterval("reload()", 1000 * 60 * 5);
    </script>
</head>
<body>
<h5>
    <form action="" method="get">
        <a href="/nic/">首页</a> | <a href="/netview/">监控首页</a> | 查询：
        <input type="text" name="demandKey" placeholder="地址或者ip的一部分" required>
        <input type="submit" value="查询">
    </form>
</h5>

<%--<c:if test="${youNicer!=null&&youNicer.status==youNicer.adminStatus}">--%>
<h5>
    ip：<input type="text" name="address" placeholder="127.0.0.1">
    楼栋：<input type="text" name="building" placeholder="A1">
    楼层：<input type="text" name="floor" placeholder="1F/601">
    机型：<input type="text" name="model" placeholder="S2352">
    名字：<input type="text" name="name" placeholder="选填">
    <input type="button" value="添加" onclick="addHost()">
</h5>

<h5>
    <form action="" method="post" enctype="multipart/form-data">
        批量添加（xls）：
        <input type="file" name="ipFile" required>
        <input type="submit" value="添加">
        <a href="/netview/file/ip.xls"> excel模板</a>
    </form>
</h5>
<%--</c:if>--%>



<c:forEach var="building" items="${buildings}">
    <h3 class="buildingName" onclick="display('${building.buildingName}')"><a>${building.buildingName}</a></h3>
    <table name="${building.buildingName}">
        <tr>
            <td>地址</td>
                <%--<c:if test="${youNicer!=null&&youNicer.status==youNicer.adminStatus}">--%>
            <td>ip</td>
                <%--</c:if>--%>
            <td>延时</td>
            <td>日期</td>
                <%--<c:if test="${youNicer!=null&&youNicer.status==youNicer.adminStatus}">--%>
            <td>删除</td>
                <%--</c:if>--%>
        </tr>
        <c:forEach var="host" items="${building.hosts}">
            <c:if test="${host.conn}">
                <tr class="connTr">
            </c:if>
            <c:if test="${!host.conn}">
                <tr class="notConnTr">
            </c:if>

            <c:if test="${host.name==null||host.name==''}">
                <td>${host.building}-${host.floor}-${host.model}</td>
            </c:if>
            <c:if test="${host.name!=null&&host.name!=''}">
                <td>${host.building}-${host.floor}-${host.model}-${host.name}</td>
            </c:if>

            <%--<c:if test="${youNicer!=null&&youNicer.status==youNicer.adminStatus}">--%>
            <td>${host.address}</td>
            <%--</c:if>--%>

            <td>
                <c:forEach var="result" items="${host.results}">
                <c:if test="${result.delay>=0}">
                <div class="conn">
                    </c:if>
                    <c:if test="${result.delay<0}">
                    <div class="notConn">
                        </c:if>
                            ${result.delay}
                    </div>
                    </c:forEach>
            </td>

            <td>
                <c:if test="${host.conn}">
                    恢复时间：<fmt:formatDate value="${host.date}" pattern="MM-dd hh:mm:ss"/>
                </c:if>
                <c:if test="${!host.conn}">
                    断开时间：<fmt:formatDate value="${host.date}" pattern="MM-dd hh:mm:ss"/>
                </c:if>
            </td>

            <%--<c:if test="${youNicer!=null&&youNicer.status==youNicer.adminStatus}">--%>
                <td><a href="" onclick="deleteHost('${host.address}')">删除</a></td>
            <%--</c:if>--%>
            </tr>
        </c:forEach>
    </table>
</c:forEach>
</body>
</html>
