<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String ctx = request.getContextPath();
    List<Reservation> list = (List<Reservation>) request.getAttribute("list");
    Reservation r = (Reservation) request.getAttribute("reservation");
%>

<!DOCTYPE html>
<html>
<head>
    <title>View Reservations - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        /* Page-only styles (safe) */
        .toolbar{
            display:flex;
            justify-content:space-between;
            align-items:flex-end;
            gap:12px;
            flex-wrap:wrap;
            margin-top: 6px;
        }
        .toolbar .left h2{ margin:0; }
        .toolbar .left p{ margin:6px 0 0; }

        .search-box{
            display:flex;
            gap:10px;
            align-items:flex-end;
            flex-wrap:wrap;
        }
        .field{
            display:flex;
            flex-direction:column;
        }
        .field label{
            font-size: 12.5px;
            font-weight: 800;
            color: var(--muted);
            margin-bottom: 6px;
        }
        .input{
            padding: 11px 12px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background:#fff;
            outline:none;
            min-width: 240px;
            font-size: 14px;
        }
        .input:focus{
            border-color: rgba(37,99,235,.40);
            box-shadow: 0 0 0 4px rgba(37,99,235,.10);
        }

        .btn{
            padding: 11px 14px;
            border-radius: 14px;
            border: 1px solid rgba(15,23,42,.12);
            background: var(--primary);
            color:#fff;
            font-weight: 900;
            cursor:pointer;
        }
        .btn:hover{ opacity:.95; }

        .btn.secondary{
            background:#fff;
            color: var(--text);
        }
        .btn.secondary:hover{ background: rgba(15,23,42,.04); }

        .details{
            margin-top: 14px;
            border: 1px solid var(--border);
            border-radius: var(--radius);
            background:#fff;
            box-shadow: var(--shadow2);
            padding: 14px 16px;
        }
        .details h3{
            margin:0 0 10px;
            font-size: 15.5px;
        }
        .d-grid{
            display:grid;
            grid-template-columns: 220px 1fr;
            gap: 8px 14px;
        }
        .d-label{
            color: var(--muted);
            font-weight: 800;
            font-size: 12.5px;
        }
        .d-val{
            font-size: 13.5px;
            color: var(--text);
        }
        @media(max-width: 900px){
            .d-grid{ grid-template-columns: 1fr; }
        }

        .table-wrap{ margin-top: 14px; }
        table{
            width:100%;
            border-collapse: collapse;
            border: 1px solid var(--border);
            border-radius: var(--radius);
            overflow:hidden;
            background:#fff;
        }
        th, td{
            padding: 12px 12px;
            border-bottom: 1px solid rgba(15,23,42,.10);
            text-align:left;
            font-size: 13.5px;
        }
        th{
            background: rgba(15,23,42,.03);
            font-size: 12px;
            letter-spacing:.3px;
            text-transform: uppercase;
            color: rgba(15,23,42,.75);
        }
        tr:hover td{
            background: rgba(37,99,235,.05);
        }

        .view-link{
            display:inline-block;
            text-decoration:none;
            padding: 8px 12px;
            border-radius: 12px;
            border: 1px solid rgba(37,99,235,.22);
            background: rgba(37,99,235,.08);
            font-weight: 900;
            font-size: 12.5px;
            color: rgba(15,23,42,.92);
        }
        .view-link:hover{ background: rgba(37,99,235,.12); }

        .muted-note{
            color: var(--muted);
            font-size: 12.5px;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <div class="welcome-card">
        <div class="welcome-title">View Reservations</div>
        <div class="welcome-sub">Browse all reservations, search by ID, or click “View” to see full details.</div>
    </div>

    <div class="panel">

        <div class="toolbar">
            <div class="left">
                <h2 class="h1">Reservation List</h2>
                <p class="sub">Use the search box to find a reservation by ID.</p>
            </div>

            <!-- ✅ Search by ID (POST) -->
            <form class="search-box" method="post" action="<%= ctx %>/viewReservation">
                <div class="field">
                    <label>Reservation ID</label>
                    <input class="input" name="reservationId" placeholder="e.g. 3" required>
                </div>
                <button class="btn" type="submit">Search</button>
                <a class="btn secondary" href="<%= ctx %>/viewReservation" style="text-decoration:none; display:inline-block;">Reset</a>
            </form>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <!-- ✅ Details panel (only shows when reservation found) -->
        <% if (r != null) { %>
            <div class="details">
                <h3>Reservation Details (ID: <%= r.getReservationId() %>)</h3>

                <div class="d-grid">
                    <div class="d-label">Guest Name</div><div class="d-val"><%= r.getGuestName() %></div>
                    <div class="d-label">Guest Address</div><div class="d-val"><%= r.getGuestAddress() %></div>
                    <div class="d-label">Contact Number</div><div class="d-val"><%= r.getContactNumber() %></div>
                    <div class="d-label">Room Type</div><div class="d-val"><%= r.getRoomType() %></div>
                    <div class="d-label">Check-in Date</div><div class="d-val"><%= r.getCheckInDate() %></div>
                    <div class="d-label">Check-out Date</div><div class="d-val"><%= r.getCheckOutDate() %></div>
                </div>

                <div class="muted-note">
                    Tip: Use <b>Billing</b> menu to generate and print the bill using this Reservation ID.
                </div>
            </div>
        <% } %>

        <!-- ✅ All reservations table -->
        <div class="table-wrap">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Guest</th>
                    <th>Contact</th>
                    <th>Room</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Action</th>
                </tr>

                <%
                    if (list != null && !list.isEmpty()) {
                        for (Reservation item : list) {
                %>
                    <tr>
                        <td><%= item.getReservationId() %></td>
                        <td><%= item.getGuestName() %></td>
                        <td><%= item.getContactNumber() %></td>
                        <td><%= item.getRoomType() %></td>
                        <td><%= item.getCheckInDate() %></td>
                        <td><%= item.getCheckOutDate() %></td>
                        <td>
                            <!-- ✅ View (GET) -->
                            <a class="view-link" href="<%= ctx %>/viewReservation?id=<%= item.getReservationId() %>">View</a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="7">No reservations found.</td>
                    </tr>
                <% } %>
            </table>
        </div>

    </div>
</div>

</body>
</html>