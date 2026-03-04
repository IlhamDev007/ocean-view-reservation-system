<%
  String ctx = request.getContextPath();
%>

<div class="navbar">
  <div class="navbar-inner">
    <a class="brand" href="<%= ctx %>/dashboard">
      <div class="brand-badge">OV</div>
      <div class="brand-title">
        <b>OceanView</b>
        <span>Room Reservation System</span>
      </div>
    </a>

    <div class="nav-actions">
      <a class="nav-link" href="<%= ctx %>/dashboard">Home</a>
      <a class="nav-btn danger" href="<%= ctx %>/logout">Logout</a>
    </div>
  </div>
</div>