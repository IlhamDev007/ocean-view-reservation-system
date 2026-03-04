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
    String q = (String) request.getAttribute("q");
    String msg = request.getParameter("msg");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Reservations - OceanView</title>
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
            min-width: 300px;
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
            text-decoration:none;
            display:inline-block;
        }
        .btn:hover{ opacity:.95; }

        .btn.secondary{
            background:#fff;
            color: var(--text);
        }
        .btn.secondary:hover{ background: rgba(15,23,42,.04); }

        .btn-edit{
            border: 1px solid rgba(147,51,234,.25);
            background: rgba(147,51,234,.10);
            color: rgba(15,23,42,.92);
            font-weight: 900;
            padding: 8px 12px;
            border-radius: 12px;
            text-decoration:none;
            display:inline-block;
        }
        .btn-edit:hover{ background: rgba(147,51,234,.14); }

        .btn-del{
            border: 1px solid rgba(220,38,38,.30);
            background: rgba(220,38,38,.10);
            color: rgba(220,38,38,.95);
            font-weight: 900;
            padding: 8px 12px;
            border-radius: 12px;
            cursor:pointer;
        }
        .btn-del:hover{ background: rgba(220,38,38,.14); }

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
            vertical-align: middle;
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

        .actions{
            display:flex;
            gap:10px;
            align-items:center;
            flex-wrap:wrap;
        }
        form.inline{ display:inline; margin:0; }

        .msgBox{
            margin-top: 12px;
            padding: 12px 14px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background: rgba(15,23,42,.03);
            font-size: 13px;
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <div class="welcome-card">
        <div class="welcome-title">Manage Reservations</div>
        <div class="welcome-sub">Search reservations, edit details, or delete records safely.</div>
    </div>

    <div class="panel">

        <div class="toolbar">
            <div class="left">
                <h2 class="h1">Reservation Management</h2>
                <p class="sub">Use keyword search (ID / Name / Contact / Room Type / Address).</p>
            </div>

            <!-- ✅ Search (GET) -->
            <form class="search-box" method="get" action="<%= ctx %>/manageReservations">
                <div class="field">
                    <label>Search</label>
                    <input class="input" name="q" placeholder="Type ID, name, contact, room type..." value="<%= (q==null ? "" : q) %>">
                </div>
                <button class="btn" type="submit">Search</button>
                <a class="btn secondary" href="<%= ctx %>/manageReservations">Reset</a>
                <a class="btn secondary" href="<%= ctx %>/addReservation">Add New</a>
            </form>
        </div>

        <!-- Messages -->
        <%
            if (msg != null) {
                String text;
                if ("updated".equals(msg)) text = "✅ Reservation updated successfully!";
                else if ("deleted".equals(msg)) text = "✅ Reservation deleted successfully!";
                else if ("notupdated".equals(msg)) text = "❌ Update failed.";
                else if ("notdeleted".equals(msg)) text = "❌ Delete failed.";
                else text = "❌ Something went wrong.";
        %>
            <div class="msgBox"><%= text %></div>
        <%
            }
        %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <!-- ✅ Table -->
        <div class="table-wrap">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Guest</th>
                    <th>Contact</th>
                    <th>Room</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Actions</th>
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
                            <div class="actions">
                                <!-- ✅ Edit (GET) -->
                                <a class="btn-edit" href="<%= ctx %>/manageReservations/edit?id=<%= item.getReservationId() %>">Edit</a>

                                <!-- ✅ Delete (POST) -->
                                <form class="inline" method="post" action="<%= ctx %>/manageReservations/delete"
                                      onsubmit="return confirm('Delete Reservation ID <%= item.getReservationId() %>?');">
                                    <input type="hidden" name="id" value="<%= item.getReservationId() %>">
                                    <button class="btn-del" type="submit">Delete</button>
                                </form>
                            </div>
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