<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.DashboardMetrics" %>

<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    DashboardMetrics m = (DashboardMetrics) request.getAttribute("metrics");

    int totalReservations = (m == null) ? 0 : m.getTotalReservations();
    int totalRooms = (m == null) ? 50 : m.getTotalRooms();
    int availableRooms = (m == null) ? (totalRooms - totalReservations) : m.getAvailableRooms();
    double totalRevenue = (m == null) ? 0.0 : m.getTotalRevenue();

    String loggedUser = String.valueOf(session.getAttribute("loggedUser"));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - OceanView</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css">
    <script defer src="<%= request.getContextPath() %>/assets/app.js"></script>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <!-- ✅ Welcome Card (between navbar and dashboard panel) -->
    <div class="welcome-card">
        <div class="welcome-title">Welcome Back! <%= loggedUser %> 👋</div>
        <div class="welcome-sub">Relax, Recharge, Reserve Your Perfect Stay.</div>
    </div>

    <div class="panel">
        <div class="flex">
            <div>
                <h2 class="h1">OceanView Dashboard</h2>
                <p class="sub">Here is your live system summary.</p>
            </div>
            <div class="right sub">
                Logged in as: <b><%= loggedUser %></b>
            </div>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <!-- Metrics cards -->
        <div class="grid-3" style="margin-top:16px;">
            <div class="metric">
                <div class="label">Total Reservations</div>
                <div class="value"><%= totalReservations %></div>
            </div>

            <div class="metric">
                <div class="label">Available Rooms</div>
                <div class="value"><%= availableRooms %></div>
            </div>

            <div class="metric">
                <div class="label">Total Revenue (LKR)</div>
                <div class="value"><%= String.format("%.2f", totalRevenue) %></div>
            </div>
        </div>

        <!-- Divider line -->
        <div class="divider"></div>

        <!-- Features section -->
        <div class="section">
            <div class="section-title">
                <h3>Features</h3>
                <p>Quick access to the main modules</p>
            </div>

            <div class="menu">
                <a class="tile tile-add" href="<%= request.getContextPath() %>/addReservation">
                    <div>
                        <div class="t-title">Add Reservation</div>
                        <div class="t-sub">Create a new booking</div>
                    </div>
                    <span class="chip">➕</span>
                </a>

                <a class="tile tile-view" href="<%= request.getContextPath() %>/viewReservation">
                    <div>
                        <div class="t-title">View Reservations</div>
                        <div class="t-sub">View all, search, open details</div>
                    </div>
                    <span class="chip">🔎</span>
                </a>

                <a class="tile tile-manage" href="<%= request.getContextPath() %>/manageReservations">
                    <div>
                        <div class="t-title">Manage Reservations</div>
                        <div class="t-sub">Edit, delete, search</div>
                    </div>
                    <span class="chip">🛠</span>
                </a>

                <a class="tile tile-bill" href="<%= request.getContextPath() %>/bill">
                    <div>
                        <div class="t-title">Billing</div>
                        <div class="t-sub">Calculate & print invoices</div>
                    </div>
                    <span class="chip">🧾</span>
                </a>

                <a class="tile tile-help" href="<%= request.getContextPath() %>/help.jsp">
                    <div>
                        <div class="t-title">Help</div>
                        <div class="t-sub">How to use the system</div>
                    </div>
                    <span class="chip">❓</span>
                </a>

                <!-- ✅ NEW Logout feature tile -->
                <a class="tile tile-logout" href="<%= request.getContextPath() %>/logout">
                    <div>
                        <div class="t-title">Logout</div>
                        <div class="t-sub">End your session return.</div>
                    </div>
                    <span class="chip">⎋</span>
                </a>
            </div>
        </div>

    </div>
</div>

</body>
</html>