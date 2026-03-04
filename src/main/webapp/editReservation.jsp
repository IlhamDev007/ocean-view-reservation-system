<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String ctx = request.getContextPath();

    Reservation r = (Reservation) request.getAttribute("reservation");
    if (r == null) {
        response.sendRedirect(ctx + "/manageReservations");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Reservation - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        /* Page-only styles */
        .form-grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
            margin-top: 14px;
        }
        .field{ display:flex; flex-direction:column; }
        .field label{
            font-size: 12.5px;
            font-weight: 800;
            color: var(--muted);
            margin-bottom: 6px;
        }
        .input, select{
            padding: 11px 12px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background:#fff;
            outline:none;
            font-size: 14px;
        }
        .input:focus, select:focus{
            border-color: rgba(37,99,235,.40);
            box-shadow: 0 0 0 4px rgba(37,99,235,.10);
        }
        .full{ grid-column: 1 / -1; }

        .actions{
            display:flex;
            gap:10px;
            flex-wrap:wrap;
            margin-top: 16px;
        }

        .btn{
            display:inline-block;
            padding: 11px 14px;
            border-radius: 14px;
            border: 1px solid rgba(15,23,42,.12);
            background: var(--primary);
            color:#fff;
            font-weight: 900;
            cursor:pointer;
            text-decoration:none;
        }
        .btn:hover{ opacity:.95; }

        .btn.secondary{
            background:#fff;
            color: var(--text);
        }
        .btn.secondary:hover{ background: rgba(15,23,42,.04); }

        .pill{
            display:inline-block;
            padding: 6px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 900;
            background: rgba(37,99,235,.10);
            border: 1px solid rgba(37,99,235,.20);
            color: rgba(15,23,42,.90);
            margin-left: 8px;
        }

        @media (max-width: 900px){
            .form-grid{ grid-template-columns: 1fr; }
            .full{ grid-column:auto; }
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <div class="welcome-card">
        <div class="welcome-title">Edit Reservation <span class="pill">ID: <%= r.getReservationId() %></span></div>
        <div class="welcome-sub">Update the reservation details and save changes.</div>
    </div>

    <div class="panel">

        <div class="flex">
            <div>
                <h2 class="h1">Reservation Update Form</h2>
                <p class="sub">Modify the values below and click “Update Reservation”.</p>
            </div>
            <div class="right sub">
                Logged in as: <b><%= session.getAttribute("loggedUser") %></b>
            </div>
        </div>

        <!-- ✅ Keep action and input names unchanged -->
        <form method="post" action="<%= ctx %>/manageReservations/update">

            <input type="hidden" name="reservationId" value="<%= r.getReservationId() %>">

            <div class="form-grid">

                <div class="field full">
                    <label>Guest Name</label>
                    <input class="input" name="guestName" value="<%= r.getGuestName() %>" required>
                </div>

                <div class="field full">
                    <label>Guest Address</label>
                    <input class="input" name="guestAddress" value="<%= r.getGuestAddress() %>" required>
                </div>

                <div class="field">
                    <label>Contact Number</label>
                    <input class="input" name="contactNumber" value="<%= r.getContactNumber() %>" required>
                </div>

                <div class="field">
                    <label>Room Type</label>
                    <select name="roomType" required>
                        <option value="Single" <%= "Single".equalsIgnoreCase(r.getRoomType()) ? "selected" : "" %>>Single</option>
                        <option value="Double" <%= "Double".equalsIgnoreCase(r.getRoomType()) ? "selected" : "" %>>Double</option>
                        <option value="Deluxe" <%= "Deluxe".equalsIgnoreCase(r.getRoomType()) ? "selected" : "" %>>Deluxe</option>
                    </select>
                </div>

                <div class="field">
                    <label>Check-in Date</label>
                    <input class="input" type="date" name="checkInDate" value="<%= r.getCheckInDate() %>" required>
                </div>

                <div class="field">
                    <label>Check-out Date</label>
                    <input class="input" type="date" name="checkOutDate" value="<%= r.getCheckOutDate() %>" required>
                </div>

            </div>

            <div class="actions">
                <button class="btn" type="submit">Update Reservation</button>
                <a class="btn secondary" href="<%= ctx %>/manageReservations">Back to Manage</a>
                <a class="btn secondary" href="<%= ctx %>/dashboard">Back to Dashboard</a>
            </div>
        </form>

    </div>
</div>

</body>
</html>