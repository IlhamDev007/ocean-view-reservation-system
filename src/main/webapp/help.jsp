<%@ page isELIgnored="true" %>
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
    <title>Help & User Guide - OceanView</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/app.css">
    <script defer src="<%= ctx %>/assets/app.js"></script>

    <style>
        .toolbar{display:flex;justify-content:space-between;align-items:flex-end;gap:12px;flex-wrap:wrap;margin-top:6px;}
        .toolbar .left h2{margin:0;}
        .toolbar .left p{margin:6px 0 0;}

        .searchbar{display:flex;gap:10px;flex-wrap:wrap;align-items:flex-end;}
        .field{display:flex;flex-direction:column;}
        .field label{font-size:12.5px;font-weight:800;color:var(--muted);margin-bottom:6px;}
        .input{padding:11px 12px;border-radius:14px;border:1px solid var(--border);background:#fff;outline:none;min-width:320px;font-size:14px;}
        .input:focus{border-color:rgba(37,99,235,.40);box-shadow:0 0 0 4px rgba(37,99,235,.10);}

        .btn{padding:11px 14px;border-radius:14px;border:1px solid rgba(15,23,42,.12);background:var(--primary);color:#fff;font-weight:900;cursor:pointer;}
        .btn:hover{opacity:.95;}
        .btn.secondary{background:#fff;color:var(--text);}
        .btn.secondary:hover{background:rgba(15,23,42,.04);}

        .result-line{margin-top:10px;font-size:13px;color:var(--muted);}
        .kbd{display:inline-block;padding:2px 8px;border-radius:999px;border:1px solid rgba(15,23,42,.12);background:rgba(15,23,42,.04);font-weight:900;font-size:12px;}
        .grid-2{display:grid;grid-template-columns:1.2fr .8fr;gap:14px;margin-top:14px;}
        @media (max-width:980px){.grid-2{grid-template-columns:1fr;}.input{min-width:240px;width:100%;}}

        .card{background:#fff;border:1px solid var(--border);border-radius:var(--radius);box-shadow:var(--shadow2);padding:14px 16px;}

        details.step{border:1px solid rgba(15,23,42,.10);border-radius:14px;padding:10px 12px;margin-top:12px;background:rgba(15,23,42,.02);}
        details.step summary{cursor:pointer;list-style:none;display:flex;align-items:center;justify-content:space-between;gap:10px;}
        details.step summary::-webkit-details-marker{display:none;}
        .step-name{margin:0;font-size:14px;font-weight:900;}
        .chip{display:inline-block;padding:6px 10px;border-radius:999px;font-size:12px;font-weight:900;background:rgba(15,23,42,.05);border:1px solid rgba(15,23,42,.12);color:rgba(15,23,42,.88);white-space:nowrap;}

        .step-body{margin-top:10px;font-size:13.5px;line-height:1.55;}
        .step-body ul{margin:8px 0 0 18px;}
        .step-body li{margin:6px 0;}

        mark.hl{background:#fff3b0;padding:0 2px;border-radius:4px;}

        .quick-links{display:grid;gap:10px;margin-top:12px;}
        .link-tile{text-decoration:none;border:1px solid var(--tileBorder);border-radius:14px;padding:12px 14px;background:#fff;display:flex;align-items:center;justify-content:space-between;gap:10px;transition:transform .12s ease, box-shadow .12s ease, border-color .12s ease;box-shadow:0 6px 16px rgba(15,23,42,.07);}
        .link-tile:hover{transform:translateY(-2px);border-color:rgba(37,99,235,.28);box-shadow:0 10px 22px rgba(15,23,42,.10);}
        .lt-title{font-weight:900;font-size:13.5px;}
        .lt-sub{margin-top:3px;color:var(--muted);font-size:12.5px;}
        .lt-icon{padding:8px 12px;border-radius:999px;background:rgba(37,99,235,.10);border:1px solid rgba(37,99,235,.20);font-weight:900;}

        .note{margin-top:12px;padding:12px 14px;border-radius:14px;border:1px solid rgba(15,23,42,.12);background:rgba(15,23,42,.03);color:var(--muted);font-size:13px;line-height:1.5;}
        code.k{background:rgba(15,23,42,.05);border:1px solid rgba(15,23,42,.10);padding:2px 6px;border-radius:8px;font-size:12.5px;}
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">
    <div class="welcome-card">
        <div class="welcome-title">Help & User Guide</div>
        <div class="welcome-sub">Step-by-step instructions to use the OceanView Room Reservation System.</div>
    </div>

    <div class="panel">

        <div class="toolbar">
            <div class="left">
                <h2 class="h1">System Help</h2>
                <p class="sub">Use search to quickly find instructions.</p>
            </div>

            <div class="searchbar">
                <div class="field">
                    <label>Search help</label>
                    <input class="input" id="helpSearch" placeholder="Type your query">
                </div>
                <button class="btn" id="btnFind" type="button">Search</button>
                <button class="btn secondary" id="btnPrev" type="button">Prev</button>
                <button class="btn secondary" id="btnNext" type="button">Next</button>
                <button class="btn secondary" id="btnClear" type="button">Clear</button>
            </div>
        </div>

        <div class="result-line">
            Tip: Press <span class="kbd">Enter</span> to search • Use <span class="kbd">Next</span>/<span class="kbd">Prev</span>.
            <span id="resultCount" style="margin-left:10px;"></span>
        </div>

        <div class="grid-2">

            <div class="card" id="helpContent">

                <details class="step" open>
                    <summary><div class="step-name">1) Login</div><span class="chip">Authentication</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Enter your <b>Username</b> and <b>Password</b> and click <b>Login</b>.</li>
                            <li>After login, you will be redirected to the <b>Dashboard</b>.</li>
                        </ul>
                    </div>
                </details>

                <details class="step">
                    <summary><div class="step-name">2) How to Add Reservation</div><span class="chip">Create</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Open <b>Add Reservation</b> from Dashboard.</li>
                            <li>Fill guest details and contact number (exactly 10 digits).</li>
                            <li>Select dates and click <b>Save Reservation</b>.</li>
                        </ul>
                        <div class="note">Contact number must be <b>exactly 10 digits</b>.</div>
                    </div>
                </details>

                <details class="step">
                    <summary><div class="step-name">3) View Reservations</div><span class="chip">Read</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Open <b>View Reservations</b> to see all reservations.</li>
                            <li>Search by ID or click <b>View</b> to open details.</li>
                        </ul>
                    </div>
                </details>

                <details class="step">
                    <summary><div class="step-name">4) Manage Reservations (Edit/Delete)</div><span class="chip">Update/Delete</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Search reservations, click <b>Edit</b> to update, or <b>Delete</b> to remove.</li>
                        </ul>
                    </div>
                </details>

                <details class="step">
                    <summary><div class="step-name">5) Billing (Generate & Print)</div><span class="chip">Invoice</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Enter Reservation ID and click <b>Generate Bill</b>.</li>
                            <li>Total = <code class="k">nights × rate</code>.</li>
                            <li>Click <b>Print Invoice</b> to print or save PDF.</li>
                        </ul>
                    </div>
                </details>

                <details class="step">
                    <summary><div class="step-name">6) Logout</div><span class="chip">Session</span></summary>
                    <div class="step-body">
                        <ul>
                            <li>Click <b>Logout</b> in navbar.</li>
                            <li>You will return to login with a logout message.</li>
                        </ul>
                    </div>
                </details>

            </div>

            <div class="card">
                <h3 style="margin:0; font-size:15.5px; font-weight:900;">Quick Links</h3>
                <p style="margin:6px 0 0; color:var(--muted); font-size:13px;">Jump to pages.</p>

                <div class="quick-links">
                    <a class="link-tile" href="<%= ctx %>/dashboard"><div><div class="lt-title">Dashboard</div><div class="lt-sub">Summary</div></div><div class="lt-icon">🏠</div></a>
                    <a class="link-tile" href="<%= ctx %>/addReservation"><div><div class="lt-title">Add Reservation</div><div class="lt-sub">Create booking</div></div><div class="lt-icon">➕</div></a>
                    <a class="link-tile" href="<%= ctx %>/viewReservation"><div><div class="lt-title">View Reservations</div><div class="lt-sub">Browse</div></div><div class="lt-icon">🔎</div></a>
                    <a class="link-tile" href="<%= ctx %>/manageReservations"><div><div class="lt-title">Manage</div><div class="lt-sub">Edit/Delete</div></div><div class="lt-icon">🛠</div></a>
                    <a class="link-tile" href="<%= ctx %>/bill"><div><div class="lt-title">Billing</div><div class="lt-sub">Invoice</div></div><div class="lt-icon">🧾</div></a>
                </div>
                <div class="note">
                    <b>Common Issues</b><br><br>
                    • If you see a “No reservation found” message, confirm the ID exists.<br>
                    • Ensure check-out date is after check-in date.<br>
                    • Use Logout to safely exit and protect your session.
                </div>

                <div class="note">
                    <b>Data Tips</b><br><br>
                    • Reservation ID is auto-generated by the database.<br>
                    • Dashboard values update automatically after Add/Delete.<br>
                    • Billing uses room type and nights to compute totals.
                </div>
            </div>

        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
  const input = document.getElementById("helpSearch");
  const btnFind = document.getElementById("btnFind");
  const btnPrev = document.getElementById("btnPrev");
  const btnNext = document.getElementById("btnNext");
  const btnClear = document.getElementById("btnClear");
  const content = document.getElementById("helpContent");
  const resultCount = document.getElementById("resultCount");

  let matches = [];
  let idx = -1;
  const originalHTML = content.innerHTML;

  function clearHighlights(){
    content.innerHTML = originalHTML;
    matches = [];
    idx = -1;
    resultCount.textContent = "";
  }

  function openParentDetails(node){
    const d = node.closest("details");
    if (d) d.open = true;
  }

  function scrollToMatch(i){
    if (!matches.length) return;
    idx = i;
    matches.forEach(m => m.style.outline = "none");
    const m = matches[idx];
    openParentDetails(m);
    m.scrollIntoView({behavior:"smooth", block:"center"});
    m.style.outline = "2px solid rgba(37,99,235,.35)";
    resultCount.textContent = `Result ${idx+1} of ${matches.length}`;
  }

  function doSearch(){
    clearHighlights();
    const q = (input.value || "").trim();
    if (!q) return;

    // ✅ Safe regex escape (no ${ in the source)
    const safe = q.replace(/[-\/\\^$*+?.()|[\]{}]/g, "\\$&");
    const re = new RegExp(safe, "gi");

    content.innerHTML = content.innerHTML.replace(re, (m) => `<mark class="hl">${m}</mark>`);
    matches = Array.from(content.querySelectorAll("mark.hl"));

    if (!matches.length){
      resultCount.textContent = "No results found";
      return;
    }
    scrollToMatch(0);
  }

  btnFind.addEventListener("click", doSearch);
  btnClear.addEventListener("click", () => { input.value=""; clearHighlights(); });
  btnNext.addEventListener("click", () => { if(matches.length) scrollToMatch((idx+1)%matches.length); });
  btnPrev.addEventListener("click", () => { if(matches.length) scrollToMatch((idx-1+matches.length)%matches.length); });

  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter"){
      e.preventDefault();
      doSearch();
    }
  });
});
</script>

</body>
</html>