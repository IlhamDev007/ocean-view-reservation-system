<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Reservation - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        /* Page-specific layout only (safe) */
        .form-grid{
            display:grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
            margin-top: 14px;
        }
        .field{ display:flex; flex-direction:column; }
        .field label{
            font-size: 12.5px;
            font-weight: 700;
            color: var(--muted);
            margin-bottom: 6px;
        }
        .input, select{
            padding: 11px 12px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background: #fff;
            outline: none;
            font-size: 14px;
        }
        .input:focus, select:focus{
            border-color: rgba(37,99,235,.40);
            box-shadow: 0 0 0 4px rgba(37,99,235,.10);
        }
        .full{ grid-column: 1 / -1; }

        .actions{
            display:flex;
            gap: 10px;
            flex-wrap:wrap;
            margin-top: 16px;
        }
        .btn{
            display:inline-block;
            padding: 11px 14px;
            border-radius: 14px;
            border: 1px solid rgba(15,23,42,.12);
            background: var(--primary);
            color: #fff;
            font-weight: 800;
            cursor:pointer;
            text-decoration:none;
        }
        .btn:hover{ opacity:.95; }

        .btn.secondary{
            background:#fff;
            color: var(--text);
        }
        .btn.secondary:hover{
            background: rgba(15,23,42,.04);
        }

        /* Small helper text */
        .help-text{
            margin-top: 6px;
            font-size: 12.5px;
            color: var(--muted);
            line-height: 1.4;
        }

        @media (max-width: 900px){
            .form-grid{ grid-template-columns: 1fr; }
            .full{ grid-column: auto; }
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <!-- Optional page intro card (same theme) -->
    <div class="welcome-card">
        <div class="welcome-title">Add Reservation</div>
        <div class="welcome-sub">Fill in guest details and booking dates, then save the reservation.</div>
    </div>

    <div class="panel">

        <div class="flex">
            <div>
                <h2 class="h1">Reservation Form</h2>
                <p class="sub">All fields are required. Contact number should contain only digits.</p>
            </div>
            <div class="right sub">
                Logged in as: <b><%= session.getAttribute("loggedUser") %></b>
            </div>
        </div>

        <!-- Messages -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert success"><%= request.getAttribute("success") %></div>
        <% } %>

        <!-- ✅ Keep action and input names unchanged -->
        <form method="post" action="<%= ctx %>/addReservation">

            <div class="form-grid">

                <div class="field full">
                    <label>Guest Name</label>
                    <input class="input" name="guestName" required placeholder="e.g., John Perera">
                </div>

                <div class="field full">
                    <label>Guest Address</label>
                    <input class="input" name="guestAddress" required placeholder="e.g., No. 12, Main Street, Colombo">
                </div>

                <div class="field">
                    <label>Contact Number</label>
                    <input class="input" name="contactNumber" required placeholder="Must be 10 Digits">
                    <div class="help-text">Example: 0771234567</div>
                </div>

                <div class="field">
                    <label>Room Type</label>
                    <select name="roomType" required>
                        <option value="">-- Select --</option>
                        <option>Single</option>
                        <option>Double</option>
                        <option>Deluxe</option>
                    </select>
                    <div class="help-text">Select the preferred room category.</div>
                </div>

                <div class="field">
                    <label>Check-in Date</label>
                    <input class="input" type="date" name="checkInDate" required>
                </div>

                <div class="field">
                    <label>Check-out Date</label>
                    <input class="input" type="date" name="checkOutDate" required>
                </div>

            </div>

            <div class="actions">
                <button class="btn" type="submit">Save Reservation</button>
                <a class="btn secondary" href="<%= ctx %>/dashboard">Back to Dashboard</a>
                <a class="btn secondary" href="<%= ctx %>/manageReservations">Manage Reservations</a>
            </div>

        </form>

    </div>
</div>

</body>
</html>