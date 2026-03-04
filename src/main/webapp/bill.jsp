<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String ctx = request.getContextPath();
    Reservation r = (Reservation) request.getAttribute("reservation");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Billing - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        /* Page-only styles */
        .toolbar{
            display:flex;
            justify-content:space-between;
            align-items:flex-end;
            gap:12px;
            flex-wrap:wrap;
        }

        .search-box{
            display:flex;
            gap:10px;
            align-items:flex-end;
            flex-wrap:wrap;
        }
        .field{ display:flex; flex-direction:column; }
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
            text-decoration:none;
            display:inline-block;
        }
        .btn:hover{ opacity:.95; }

        .btn.secondary{
            background:#fff;
            color: var(--text);
        }
        .btn.secondary:hover{ background: rgba(15,23,42,.04); }

        .invoice{
            margin-top: 14px;
            border: 1px solid var(--border);
            border-radius: var(--radius);
            background:#fff;
            box-shadow: var(--shadow2);
            padding: 14px 16px;
        }
        .invoice-top{
            display:flex;
            justify-content:space-between;
            align-items:flex-start;
            gap:12px;
            flex-wrap:wrap;
            margin-bottom: 10px;
        }
        .invoice-top h3{
            margin:0;
            font-size: 16px;
            font-weight: 900;
        }
        .invoice-top .meta{
            color: var(--muted);
            font-size: 12.5px;
            line-height: 1.4;
        }

        table.bill{
            width:100%;
            border-collapse: collapse;
            border: 1px solid var(--border);
            border-radius: var(--radius);
            overflow:hidden;
            margin-top: 10px;
            background:#fff;
        }
        table.bill td, table.bill th{
            padding: 12px;
            border-bottom: 1px solid rgba(15,23,42,.10);
            text-align:left;
            font-size: 13.5px;
        }
        table.bill th{
            background: rgba(15,23,42,.03);
            font-size: 12px;
            letter-spacing:.3px;
            text-transform: uppercase;
            color: rgba(15,23,42,.75);
        }
        .total-row td{
            font-weight: 900;
        }

        .actions{
            display:flex;
            gap:10px;
            flex-wrap:wrap;
            margin-top: 14px;
        }

        /* ✅ PRINT FIX: show ONLY invoice, hide everything else */
        @media print{
            /* Hide everything */
            body * { visibility: hidden !important; }

            /* Show only invoice area */
            .print-area, .print-area * { visibility: visible !important; }

            /* Move invoice to top-left to print */
            .print-area{
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                margin: 0 !important;
                padding: 0 !important;
                box-shadow: none !important;
                border: none !important;
            }

            body{ background:#fff !important; }
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">

    <div class="welcome-card">
        <div class="welcome-title">Billing</div>
        <div class="welcome-sub">Enter a Reservation ID to generate the bill. You can also print the invoice.</div>
    </div>

    <div class="panel">

        <div class="toolbar">
            <div>
                <h2 class="h1">Calculate & Print Bill</h2>
                <p class="sub">Generate invoice by Reservation ID.</p>
            </div>
            <div class="right sub">Logged in as: <b><%= session.getAttribute("loggedUser") %></b></div>
        </div>

        <!-- Search area -->
        <div class="search-area" style="margin-top:14px;">
            <form class="search-box" method="post" action="<%= ctx %>/bill">
                <div class="field">
                    <label>Reservation ID</label>
                    <input class="input" name="reservationId" required placeholder="e.g. 3">
                </div>
                <button class="btn" type="submit">Generate Bill</button>
                <a class="btn secondary" href="<%= ctx %>/bill">Reset</a>
            </form>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <%
            if (r != null) {
                int nights = (Integer) request.getAttribute("nights");
                double rate = (Double) request.getAttribute("rate");
                double total = (Double) request.getAttribute("total");
        %>

        <!-- ✅ Invoice area (this will print) -->
        <div class="invoice print-area">
            <div class="invoice-top">
                <div>
                    <h3>Invoice</h3>
                    <div class="meta">Reservation ID: <b><%= r.getReservationId() %></b></div>
                </div>
                <div class="meta">
                    OceanView Room Reservation System<br>
                    Generated by: <b><%= session.getAttribute("loggedUser") %></b>
                </div>
            </div>

            <table class="bill">
                <tr><th style="width:280px;">Field</th><th>Value</th></tr>
                <tr><td>Guest Name</td><td><%= r.getGuestName() %></td></tr>
                <tr><td>Room Type</td><td><%= r.getRoomType() %></td></tr>
                <tr><td>Check-in Date</td><td><%= r.getCheckInDate() %></td></tr>
                <tr><td>Check-out Date</td><td><%= r.getCheckOutDate() %></td></tr>
                <tr><td>No. of Nights</td><td><%= nights %></td></tr>
                <tr><td>Rate per Night (LKR)</td><td><%= String.format("%.2f", rate) %></td></tr>
                <tr class="total-row"><td>Total (LKR)</td><td><%= String.format("%.2f", total) %></td></tr>
            </table>
        </div>

        <!-- Buttons (won't print) -->
        <div class="actions">
            <button class="btn" onclick="window.print(); return false;">🖨 Print Invoice</button>
            <a class="btn secondary" href="<%= ctx %>/dashboard">Back to Dashboard</a>
        </div>

        <% } %>

    </div>
</div>

</body>
</html>