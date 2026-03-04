<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // If already logged in, go dashboard (BUT allow logout message to show)
    String msg = request.getParameter("msg");
    if (session.getAttribute("loggedUser") != null && !"logout".equals(msg)) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Login - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        body{ background: var(--page); }

        /* Top-left brand header */
        .top-brand{
            width: min(1320px, calc(100% - 40px));
            margin: 16px auto 0;
            display:flex;
            align-items:center;
            gap: 12px;
        }
        .brand-badge{
            width:44px; height:44px;
            border-radius: 14px;
            background: var(--primary);
            display:grid;
            place-items:center;
            font-weight: 900;
            color:#fff;
            box-shadow: 0 10px 18px rgba(37,99,235,.22);
            flex: 0 0 auto;
            font-size: 14px;
        }
        .brand-text b{
            display:block;
            font-size: 18px;
            letter-spacing:.2px;
            color: var(--text);
        }
        .brand-text span{
            display:block;
            margin-top: 2px;
            font-size: 12.5px;
            color: var(--muted);
        }

        /* Center login form */
        .login-wrap{
            min-height: calc(100vh - 90px);
            display:grid;
            place-items:center;
            padding: 18px 0 46px;
        }
        .login-panel{
            width: min(620px, calc(100% - 40px));
            background: #fff;
            border: 1px solid var(--border);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 30px 28px;
        }

        .welcome-in{
            margin:0;
            font-size: 24px;
            font-weight: 1000;
            letter-spacing:.2px;
            color: #1d4ed8;
        }
        .login-title{
            margin: 8px 0 0;
            font-size: 18px;
            font-weight: 900;
            color: var(--text);
            letter-spacing: .2px;
        }
        .form-sub{
            margin: 8px 0 0;
            color: var(--muted);
            font-size: 15px;
            line-height: 1.55;
        }

        .form{
            margin-top: 18px;
            display:grid;
            gap: 14px;
        }
        .field{ display:flex; flex-direction:column; }
        .field label{
            font-size: 14px;
            font-weight: 900;
            color: var(--muted);
            margin-bottom: 8px;
        }
        .input{
            padding: 14px 14px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background:#fff;
            outline:none;
            font-size: 15.5px;
        }
        .input:focus{
            border-color: rgba(37,99,235,.40);
            box-shadow: 0 0 0 4px rgba(37,99,235,.10);
        }

        .row{
            display:flex;
            align-items:center;
            justify-content:space-between;
            gap: 12px;
            flex-wrap:wrap;
            margin-top: 2px;
        }
        .toggle{
            display:flex;
            align-items:center;
            gap: 8px;
            user-select:none;
            color: var(--muted);
            font-size: 13.5px;
            font-weight: 700;
        }
        .toggle input{
            width: 18px;
            height: 18px;
            accent-color: var(--primary);
        }

        .btn{
            margin-top: 4px;
            padding: 13px 14px;
            border-radius: 14px;
            border: 1px solid rgba(15,23,42,.12);
            background: var(--primary);
            color:#fff;
            font-weight: 900;
            font-size: 15.5px;
            cursor:pointer;
            width: 100%;
        }
        .btn:hover{ opacity:.95; }

        .forgot{
            margin-top: 12px;
            text-align:center;
            font-size: 13.5px;
            color: var(--muted);
        }
        .forgot a{
            color: #1d4ed8;
            font-weight: 900;
            text-decoration:none;
        }
        .forgot a:hover{ text-decoration: underline; }

        .small{
            margin-top: 14px;
            color: var(--muted);
            font-size: 13.5px;
            line-height: 1.6;
            text-align:center;
        }
        .small code{
            background: rgba(15,23,42,.05);
            border: 1px solid rgba(15,23,42,.10);
            padding: 2px 6px;
            border-radius: 8px;
            font-size: 13px;
        }
    </style>
</head>

<body>

<!-- Top-left brand -->
<div class="top-brand">
    <div class="brand-badge">OV</div>
    <div class="brand-text">
        <b>OceanView</b>
        <span>Room Reservation System</span>
    </div>
</div>

<!-- Center login -->
<div class="login-wrap">
    <div class="login-panel">

        <h2 class="welcome-in">Welcome to OceanView</h2>
        <div class="login-title">Login</div>
        <p class="form-sub">Please login to continue to the reservation system.</p>

        <!-- ✅ Logout success message -->
        <%
            if ("logout".equals(msg)) {
        %>
            <div class="alert success">✅ You have been successfully logged out.</div>
        <%
            }
        %>

        <!-- ✅ Login error message -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form class="form" method="post" action="<%= ctx %>/login">
            <div class="field">
                <label>Username</label>
                <input class="input" name="username" required placeholder="e.g., admin">
            </div>

            <div class="field">
                <label>Password</label>
                <input class="input" id="pwd" type="password" name="password" required placeholder="Enter password">
            </div>

            <div class="row">
                <label class="toggle">
                    <input type="checkbox" id="showPwd">
                    Show Password
                </label>
            </div>

            <button class="btn" type="submit">Login</button>

            <div class="forgot">
                Forgot password?
                <a href="#" onclick="alert('Please contact: ilhamsk360@gamil.com to reset your password.'); return false;">
                    Reset here
                </a>
            </div>
        </form>

        <div class="small">
            Demo account (if configured): <code>admin</code> / <code>admin123</code>
        </div>
    </div>
</div>

<script>
  // Show/Hide password toggle
  document.addEventListener("DOMContentLoaded", () => {
    const cb = document.getElementById("showPwd");
    const pwd = document.getElementById("pwd");
    if (cb && pwd) {
      cb.addEventListener("change", () => {
        pwd.type = cb.checked ? "text" : "password";
      });
    }
  });
</script>

</body>
</html>