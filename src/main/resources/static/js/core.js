var baseUrl = "http://localhost:8080/port-scanner/api/v1/scan/"
var webContext = "http://localhost:8080/";

function simple_scan() {
    var ip = $("#ip").val();
    var port = $("#port").val();

    if (ip == null || ip.trim() === "") {
        mdui.alert('请输入域名或IP地址！', '系统提示');
        return;
    }

    if (port == null || port.trim() === "") {
        mdui.alert('请输入端口号！', '系统提示');
        return;
    }

    $("#result_ip").text(ip);
    $("#result_port").text(port);
    $("#result_status").text("扫描中...");
    $("#result_protocol").text("扫描中...");
    $("#result_service").text("扫描中...");
    $("#result_service_des").text("扫描中...");
    $("#progress").attr("style", "visibility:visible")

    var settings = {
        "url": baseUrl + "/simple",
        "method": "POST",
        "timeout": 0,
        "data": "{\n\t\"ip\":\"" + ip + "\",\n\t\"port\":\"" + port + "\"\n}",
        "headers": {
            "Content-Type": "application/json"
        }
    };

    $.ajax(settings).done(function (response) {
        console.log(response.code)
        if (response.code !== '1') {
            mdui.alert(response.message, '系统提示');
            $("#result_ip").text('');
            $("#result_port").text('');
            $("#result_status").text('');
            $("#result_protocol").text('');
            $("#result_service").text('');
            $("#result_service_des").text('');
            $("#progress").attr("style", "visibility:hidden")
            return;
        }
        var data = response.data;
        $("#result_ip").text(data.ip);
        $("#result_port").text(data.port);
        $("#result_status").text(data.status);
        $("#result_protocol").text(data.protocol);
        $("#result_service").text(data.service);
        $("#result_service_des").text(data.serviceDescription);
        $("#progress").attr("style", "visibility:hidden")

    });
}


function full_scan() {
    var startIp = $("#startIp").val();
    var endIp = $("#endIp").val();
    var startPort = $("#startPort").val();
    var endPort = $("#endPort").val();

    if (startIp == null || startIp.trim() === "") {
        mdui.alert('请输入起始IP地址！', '系统提示');
        return;
    }

    // 如果没填结束ip则默认使用起始ip作为结束
    if (endIp == null || endIp.trim() === "") {
        endIp = startIp;
    }

    if (startPort == null || startPort.trim() === "") {
        mdui.alert('请输入起始端口号！', '系统提示');
        return;
    }

    if (endPort == null || endPort.trim() === "") {
        endPort = startPort;
    }

    var settings = {
        "url": baseUrl,
        "method": "POST",
        "timeout": 0,
        "data": JSON.stringify(
            {
                "startIp": startIp, "endIp": endIp,
                "minPort": startPort, "maxPort": endPort
            }),
        "headers": {
            "Content-Type": "application/json"
        },
    };

    $.ajax(settings).done(function (response) {
            console.log(response.code)
            if (response.code !== '1') {
                mdui.alert(response.message, '系统提示');
            } else {
                mdui.alert('创建扫描任务成功，请前往[任务列表]页面查看扫描进度', '系统提示');
            }
        }
    );
}

function task() {

    var settings = {
        "url": baseUrl,
        "method": "GET",
        "timeout": 0,
    };

    var table = '   <thead>\n' +
        '            <tr>\n' +
        '                <th>#</th>\n' +
        '                <th>起始IP</th>\n' +
        '                <th>终止IP</th>\n' +
        '                <th>起始端口</th>\n' +
        '                <th>终止端口</th>\n' +
        '                <th>启动时间</th>\n' +
        '                <th>结束时间</th>\n' +
        '                <th>任务耗时</th>\n' +
        '                <th>状态</th>\n' +
        '                <th>结果</th>\n' +
        '            </tr>\n' +
        '            </thead>';

    $.ajax(settings).done(function (response) {
            console.log(response.code)
            if (response.code !== '1') {
                mdui.alert(response.message, '系统提示');
                window.clearInterval(timer);
                $("#progress").attr("style", "visibility:hidden")

            } else {
                for (let i = 0; i < response.data.length; i++) {
                    var task = response.data[i];
                    var tbody = '    <tbody>' +
                        '            <tr>\n' +
                        '                <td>' + (i + 1) + '</td>\n' +
                        '                <td>' + task.startIp + '</td>\n' +
                        '                <td>' + task.endIp + '</td>\n' +
                        '                <td>' + task.minPort + '</td>\n' +
                        '                <td>' + task.maxPort + '</td>\n' +
                        '                <td>' + task.startTime + '</td>\n' +
                        '                <td>' + task.endTime + '</td>\n' +
                        '                <td>' + task.runTime + '</td>\n' +
                        '                <td>' + task.status + '</td>\n' +
                        '                <td><a href="result.html?id=' + task.taskId + '">report</a></td>\n' +
                        '            </tr>\n' +
                        '            </tbody>  '
                    table += tbody;
                }
                $("#taskTable").html(table);
            }
        }
    );


}


function result(id) {

    if (id == null || id === "" || id === "null") {
        mdui.alert("请在[任务列表]中点击一个扫描任务的结果", "系统提示");
        return;
    }

    var settings = {
        "url": baseUrl + "/task/" + id,
        "method": "GET",
        "timeout": 0,
    };

    var taskTable = '   <thead>\n' +
        '            <tr>\n' +
        '                <th>#</th>\n' +
        '                <th>起始IP</th>\n' +
        '                <th>终止IP</th>\n' +
        '                <th>起始端口</th>\n' +
        '                <th>终止端口</th>\n' +
        '                <th>启动时间</th>\n' +
        '                <th>结束时间</th>\n' +
        '                <th>任务耗时</th>\n' +
        '                <th>状态</th>\n' +
        '                <th>结果</th>\n' +
        '            </tr>\n' +
        '            </thead>';

    var resultTable = '   <thead>\n' +
        '<tr>\n' +
        '                <th>IP地址</th>\n' +
        '                <th>端口</th>\n' +
        '                <th>状态</th>\n' +
        '                <th>协议</th>\n' +
        '                <th>服务</th>\n' +
        '                <th>描述</th>\n' +
        '            </tr>\n' +
        '            </thead>';


    $.ajax(settings).done(function (response) {
            console.log(response.code)
            if (response.code !== '1') {
                mdui.alert(response.message, '系统提示');
                window.clearInterval(timer);
                $("#progress").attr("style", "visibility:hidden")

            } else {
                var task = response.data;
                if (task.status === 'success') {
                    window.clearInterval(timer);
                    $("#progress").attr("style", "visibility:hidden")
                } else {
                    $("#progress").attr("style", "visibility:visible")
                }

                var taskTbody = '    <tbody>' +
                    '            <tr>\n' +
                    '                <td>1</td>\n' +
                    '                <td>' + task.startIp + '</td>\n' +
                    '                <td>' + task.endIp + '</td>\n' +
                    '                <td>' + task.minPort + '</td>\n' +
                    '                <td>' + task.maxPort + '</td>\n' +
                    '                <td>' + task.startTime + '</td>\n' +
                    '                <td>' + task.endTime + '</td>\n' +
                    '                <td>' + task.runTime + '</td>\n' +
                    '                <td>' + task.status + '</td>\n' +
                    '                <td><a href="result.html?id=' + task.taskId + '">result</a></td>\n' +
                    '            </tr>\n' +
                    '            </tbody>  '
                taskTable += taskTbody;
                $("#taskTable").html(taskTable);


                var openTable = resultTable;
                var closeTable = resultTable;
                var hosts = task.results;
                for (let i = 0; i < hosts.length; i++) {
                    var ports = hosts[i].portList;
                    for (let j = 0; j < ports.length; j++) {
                        var port = ports[j];
                        var portTbody;
                        if (port.complete) {

                            if (port.status === '开启') {
                                portTbody = '    <tbody>' +
                                    '            <tr>\n' +
                                    '                <td>' + port.ip + '</td>\n' +
                                    '                <td>' + port.port + '</td>\n' +
                                    '                <td>' + port.status + '</td>\n' +
                                    '                <td>' + port.protocol + '</td>\n' +
                                    '                <td>' + port.service + '</td>\n' +
                                    '                <td>' + port.serviceDescription + '</td>\n' +
                                    '            </tr>\n' +
                                    '            </tbody>  '
                                openTable += portTbody;
                            } else {
                                portTbody = '    <tbody>' +
                                    '            <tr>\n' +
                                    '                <td>' + port.ip + '</td>\n' +
                                    '                <td>' + port.port + '</td>\n' +
                                    '                <td>' + port.status + '</td>\n' +
                                    '                <td>' + port.protocol + '</td>\n' +
                                    '                <td>未知</td>\n' +
                                    '                <td>未知</td>\n' +
                                    '            </tr>\n' +
                                    '            </tbody>  '
                                closeTable += portTbody;
                            }
                        }
                    }
                }
                $("#resultTable-open").html(openTable);
                $("#resultTable-close").html(closeTable);
            }
        }
    );


}