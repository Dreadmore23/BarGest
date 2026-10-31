/* BarberGest — interacciones de interfaz EV03
   Demostración frontend: guarda los cambios en localStorage del navegador. */
(() => {
    "use strict";

    const STORAGE_KEY = "barbergest_ev03_v1";
    const $ = (selector, root = document) => root.querySelector(selector);
    const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];

    const today = new Date();
    const dateISO = (date) => {
        const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
        return local.toISOString().slice(0, 10);
    };
    const plusDays = (days) => {
        const date = new Date();
        date.setDate(date.getDate() + days);
        return dateISO(date);
    };
    const money = (value) => new Intl.NumberFormat("es-CO", {
        style: "currency", currency: "COP", maximumFractionDigits: 0
    }).format(Number(value || 0));
    const escapeHTML = (value) => String(value ?? "").replace(/[&<>"']/g, (char) => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    })[char]);
    const initials = (name) => String(name || "?").trim().split(/\s+/).slice(0, 2).map(part => part[0] || "").join("").toUpperCase();

    const seedData = () => ({
        clients: [
            { id: 1, name: "Juan Pérez", phone: "300 123 4567", email: "juan@email.com" },
            { id: 2, name: "Pedro Gómez", phone: "301 987 6543", email: "pedro@email.com" },
            { id: 3, name: "Andrés Rodríguez", phone: "315 456 7890", email: "andres@email.com" },
            { id: 4, name: "Mateo Ramírez", phone: "312 555 2030", email: "mateo@email.com" }
        ],
        barbers: [
            { id: 1, name: "Carlos", specialty: "Corte clásico y moderno", phone: "310 555 0101" },
            { id: 2, name: "Andrés", specialty: "Corte, barba y perfilado", phone: "310 555 0102" }
        ],
        services: [
            { id: 1, name: "Corte de cabello", description: "Corte clásico o moderno", duration: 30, price: 18000 },
            { id: 2, name: "Corte + barba", description: "Corte de cabello y arreglo de barba", duration: 40, price: 28000 },
            { id: 3, name: "Corte infantil", description: "Servicio de corte para niños", duration: 30, price: 15000 },
            { id: 4, name: "Corte completo", description: "Cabello, barba y perfilado de cejas", duration: 50, price: 35000 }
        ],
        appointments: [
            { id: 1, clientId: 1, barberId: 1, serviceId: 1, date: today.getHours() < 10 ? dateISO(today) : plusDays(1), time: "10:00", status: "programada", notes: "" },
            { id: 2, clientId: 2, barberId: 2, serviceId: 2, date: today.getHours() < 10 ? dateISO(today) : plusDays(1), time: "10:30", status: "programada", notes: "" },
            { id: 3, clientId: 3, barberId: 1, serviceId: 3, date: plusDays(-1), time: "14:00", status: "realizada", notes: "" }
        ]
    });

    let data;
    try {
        const saved = localStorage.getItem(STORAGE_KEY);
        data = saved ? JSON.parse(saved) : seedData();
        if (!data || !Array.isArray(data.clients) || !Array.isArray(data.appointments)) data = seedData();
    } catch {
        data = seedData();
    }
    const save = () => localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
    if (!localStorage.getItem(STORAGE_KEY)) save();

    let currentView = "inicio";
    let searchText = "";
    let appointmentFilter = "todas";
    let toastTimer;

    const clientById = id => data.clients.find(item => item.id === Number(id));
    const barberById = id => data.barbers.find(item => item.id === Number(id));
    const serviceById = id => data.services.find(item => item.id === Number(id));
    const formatDate = value => {
        if (!value) return "—";
        const [y, m, d] = value.split("-").map(Number);
        return new Date(y, m - 1, d).toLocaleDateString("es-CO", { day: "2-digit", month: "short", year: "numeric" });
    };
    const statusBadge = status => `<span class="status status-${escapeHTML(status)}">${escapeHTML(status.charAt(0).toUpperCase() + status.slice(1))}</span>`;
    const person = name => `<div class="person-cell"><span class="mini-avatar">${escapeHTML(initials(name))}</span><span><strong>${escapeHTML(name || "Sin asignar")}</strong></span></div>`;
    const empty = (title, message) => `<div class="empty-state"><strong>${escapeHTML(title)}</strong>${escapeHTML(message)}</div>`;
    const setTitle = title => { $("#pageTitle").textContent = title; document.title = `BarberGest | ${title}`; };
    const showToast = message => {
        const toast = $("#toast");
        toast.textContent = message;
        toast.classList.add("visible");
        clearTimeout(toastTimer);
        toastTimer = setTimeout(() => toast.classList.remove("visible"), 2800);
    };
    const goTo = view => {
        currentView = view;
        searchText = "";
        appointmentFilter = "todas";
        $$(".menu-item").forEach(link => link.classList.toggle("activo", link.dataset.view === view));
        $("#sidebar").classList.remove("open");
        $("#mobileMenu").setAttribute("aria-expanded", "false");
        render();
        window.location.hash = view;
    };

    function render() {
        const view = ({
            inicio: renderDashboard,
            clientes: renderClients,
            citas: renderAppointments,
            barberos: renderBarbers,
            servicios: renderServices,
            historial: renderHistory
        })[currentView] || renderDashboard;
        view();
        $("#appView").focus({ preventScroll: true });
    }

    function renderDashboard() {
        setTitle("Inicio");
        const todayString = dateISO(new Date());
        const todayAppointments = data.appointments.filter(a => a.date === todayString && a.status === "programada");
        $("#appView").innerHTML = `
            <section class="welcome">
                <div><p class="eyebrow">PANEL DE CONTROL</p><h1>¡Bienvenido a BarberGest!</h1><p class="muted">Organiza las citas y mantén la información de tu barbería en un solo lugar.</p></div>
                <button class="btn btn-primary" data-action="new-appointment">＋ Nueva cita</button>
            </section>
            <section class="stats-grid" aria-label="Resumen de la barbería">
                <article class="stat-card"><div class="stat-top"><span class="stat-label">Citas de hoy</span><span class="stat-icon purple">▦</span></div><strong>${todayAppointments.length}</strong><span class="stat-foot">Citas programadas para hoy</span></article>
                <article class="stat-card"><div class="stat-top"><span class="stat-label">Clientes registrados</span><span class="stat-icon blue">♙</span></div><strong>${data.clients.length}</strong><span class="stat-foot">Clientes en el registro</span></article>
                <article class="stat-card"><div class="stat-top"><span class="stat-label">Barberos</span><span class="stat-icon orange">✂</span></div><strong>${data.barbers.length}</strong><span class="stat-foot">Profesionales registrados</span></article>
                <article class="stat-card"><div class="stat-top"><span class="stat-label">Servicios</span><span class="stat-icon green">◈</span></div><strong>${data.services.length}</strong><span class="stat-foot">Servicios disponibles</span></article>
            </section>
            <section class="panel">
                <div class="panel-heading"><div><h2>Próximas citas</h2><p class="muted">Agenda pendiente y programada.</p></div><button class="btn btn-secondary btn-small" data-navigate="citas">Ver todas →</button></div>
                <div class="table-wrap">${appointmentsTable(data.appointments.filter(a => a.status === "programada").sort(sortAppointments).slice(0, 5), false)}</div>
            </section>
            <p class="demo-note"><span>ⓘ</span> Esta versión es una interfaz demostrativa. Los datos se guardan en el almacenamiento local de este navegador.</p>`;
    }

    function sortAppointments(a, b) {
        return `${a.date}T${a.time}`.localeCompare(`${b.date}T${b.time}`);
    }

    function appointmentsTable(items, includeActions = true) {
        if (!items.length) return empty("No hay citas para mostrar", "Puedes registrar una nueva cita con el botón superior.");
        return `<table><thead><tr><th>Cliente</th><th>Barbero</th><th>Servicio</th><th>Fecha y hora</th><th>Estado</th>${includeActions ? "<th>Acciones</th>" : ""}</tr></thead><tbody>
        ${items.map(a => {
            const c = clientById(a.clientId), b = barberById(a.barberId), s = serviceById(a.serviceId);
            return `<tr><td>${person(c?.name || "Cliente eliminado")}</td><td>${escapeHTML(b?.name || "—")}</td><td>${escapeHTML(s?.name || "—")}</td><td>${formatDate(a.date)} · ${escapeHTML(a.time)}</td><td>${statusBadge(a.status)}</td>${includeActions ? `<td><div class="actions">${a.status === "programada" ? `<button class="action-btn" data-action="complete-appointment" data-id="${a.id}">Realizada</button><button class="action-btn" data-action="cancel-appointment" data-id="${a.id}">Cancelar</button>` : ""}<button class="action-btn" data-action="edit-appointment" data-id="${a.id}">Editar</button><button class="action-btn delete" data-action="delete-appointment" data-id="${a.id}">Eliminar</button></div></td>` : ""}</tr>`;
        }).join("")}</tbody></table>`;
    }

    function pageHeading(title, subtitle, buttonText, action) {
        return `<section class="page-heading"><div><p class="eyebrow">GESTIÓN</p><h1>${title}</h1><p>${subtitle}</p></div><button class="btn btn-primary" data-action="${action}">＋ ${buttonText}</button></section>`;
    }

    function renderClients() {
        setTitle("Clientes");
        const filtered = data.clients.filter(c => `${c.name} ${c.phone} ${c.email}`.toLowerCase().includes(searchText.toLowerCase()));
        $("#appView").innerHTML = `${pageHeading("Clientes", "Consulta y administra la información de tus clientes.", "Nuevo cliente", "new-client")}
        <div class="toolbar"><label class="search-box"><span class="search-icon">⌕</span><input id="searchInput" type="search" placeholder="Buscar por nombre, teléfono o correo..." value="${escapeHTML(searchText)}" aria-label="Buscar clientes"></label><span class="muted">${filtered.length} cliente(s)</span></div>
        <section class="data-panel"><div class="table-wrap">${filtered.length ? `<table><thead><tr><th>Cliente</th><th>Teléfono</th><th>Correo electrónico</th><th>Citas</th><th>Acciones</th></tr></thead><tbody>${filtered.map(c => `<tr><td>${person(c.name)}</td><td>${escapeHTML(c.phone)}</td><td>${escapeHTML(c.email || "—")}</td><td>${data.appointments.filter(a => a.clientId === c.id).length}</td><td><div class="actions"><button class="action-btn" data-action="edit-client" data-id="${c.id}">Editar</button><button class="action-btn delete" data-action="delete-client" data-id="${c.id}">Eliminar</button></div></td></tr>`).join("")}</tbody></table>` : empty("No se encontraron clientes", searchText ? "Prueba con otro término de búsqueda." : "Registra tu primer cliente con el botón Nuevo cliente.")}</div></section>`;
        bindSearch();
    }

    function renderAppointments() {
        setTitle("Citas");
        const filtered = data.appointments.filter(a => {
            const c = clientById(a.clientId);
            const matchesText = `${c?.name || ""} ${barberById(a.barberId)?.name || ""} ${serviceById(a.serviceId)?.name || ""}`.toLowerCase().includes(searchText.toLowerCase());
            const matchesStatus = appointmentFilter === "todas" || a.status === appointmentFilter;
            return matchesText && matchesStatus;
        }).sort(sortAppointments);
        $("#appView").innerHTML = `${pageHeading("Citas", "Registra y controla las citas de la barbería.", "Nueva cita", "new-appointment")}
        <div class="toolbar"><label class="search-box"><span class="search-icon">⌕</span><input id="searchInput" type="search" placeholder="Buscar cliente, barbero o servicio..." value="${escapeHTML(searchText)}" aria-label="Buscar citas"></label><select class="filter-select" id="statusFilter" aria-label="Filtrar por estado"><option value="todas">Todos los estados</option><option value="programada">Programada</option><option value="realizada">Realizada</option><option value="cancelada">Cancelada</option></select></div>
        <section class="data-panel"><div class="table-wrap">${appointmentsTable(filtered)}</div></section>`;
        $("#statusFilter").value = appointmentFilter;
        bindSearch();
        $("#statusFilter").addEventListener("change", event => { appointmentFilter = event.target.value; renderAppointments(); });
    }

    function renderBarbers() {
        setTitle("Barberos");
        $("#appView").innerHTML = `${pageHeading("Barberos", "Profesionales registrados en BarberGest.", "Nuevo barbero", "new-barber")}
        <div class="barber-grid">${data.barbers.map(b => `<article class="info-card"><div class="barber-person"><div class="barber-avatar">${escapeHTML(initials(b.name))}</div><div><h3>${escapeHTML(b.name)}</h3><p>${escapeHTML(b.specialty || "Barbero")}</p></div></div><p>Teléfono: ${escapeHTML(b.phone || "No registrado")}</p><div class="barber-card-meta"><span>Citas registradas</span><strong>${data.appointments.filter(a => a.barberId === b.id).length}</strong></div><div class="actions" style="margin-top:14px"><button class="action-btn" data-action="edit-barber" data-id="${b.id}">Editar</button><button class="action-btn delete" data-action="delete-barber" data-id="${b.id}">Eliminar</button></div></article>`).join("") || empty("No hay barberos registrados", "Agrega un barbero para asignar citas.")}</div>`;
    }

    function renderServices() {
        setTitle("Servicios");
        $("#appView").innerHTML = `${pageHeading("Servicios", "Catálogo de servicios ofrecidos por la barbería.", "Nuevo servicio", "new-service")}
        <div class="service-grid">${data.services.map(s => `<article class="info-card"><div class="info-card-top"><span class="service-symbol">✂</span><button class="btn-plain" data-action="edit-service" data-id="${s.id}" aria-label="Editar ${escapeHTML(s.name)}">Editar</button></div><h3>${escapeHTML(s.name)}</h3><p>${escapeHTML(s.description || "Servicio de barbería")}</p><div class="price">${money(s.price)}</div><span class="duration">◷ ${Number(s.duration)} minutos aprox.</span><div class="barber-card-meta"><span>En citas</span><strong>${data.appointments.filter(a => a.serviceId === s.id).length}</strong></div><button class="action-btn delete" style="margin-top:12px" data-action="delete-service" data-id="${s.id}">Eliminar servicio</button></article>`).join("") || empty("No hay servicios registrados", "Agrega los servicios de la barbería.")}</div>`;
    }

    function renderHistory() {
        setTitle("Historial");
        const history = data.appointments.filter(a => a.status !== "programada").sort((a, b) => sortAppointments(b, a));
        const completed = history.filter(a => a.status === "realizada").length;
        const cancelled = history.filter(a => a.status === "cancelada").length;
        $("#appView").innerHTML = `${pageHeading("Historial de citas", "Consulta las citas realizadas y las que fueron canceladas.", "", "") .replace('<button class="btn btn-primary" data-action="">＋ </button>', "")}
        <div class="history-summary"><div class="summary-chip">Citas realizadas <strong>${completed}</strong></div><div class="summary-chip">Citas canceladas <strong>${cancelled}</strong></div><div class="summary-chip">Total en historial <strong>${history.length}</strong></div></div>
        <section class="panel"><div class="panel-heading"><div><h2>Registro de actividad</h2><p class="muted">Citas que ya finalizaron su ciclo.</p></div></div><div class="table-wrap">${appointmentsTable(history)}</div></section>`;
    }

    function bindSearch() {
        const input = $("#searchInput");
        if (!input) return;
        input.addEventListener("input", event => {
            const cursor = event.target.selectionStart;
            searchText = event.target.value;
            const scrollY = window.scrollY;
            render();
            const replacement = $("#searchInput");
            replacement?.focus();
            replacement?.setSelectionRange(cursor, cursor);
            window.scrollTo(0, scrollY);
        });
    }

    const modalOpen = (title, body) => {
        $("#modalTitle").textContent = title;
        $("#modalBody").innerHTML = body;
        $("#modalBackdrop").hidden = false;
        document.body.style.overflow = "hidden";
        const first = $("#modalBody input, #modalBody select");
        if (first) first.focus();
    };
    const modalClose = () => {
        $("#modalBackdrop").hidden = true;
        $("#modalBody").innerHTML = "";
        document.body.style.overflow = "";
    };
    const field = (label, name, value = "", type = "text", required = true, extra = "") =>
        `<div class="field"><label for="${name}">${label}${required ? " *" : ""}</label><input id="${name}" name="${name}" type="${type}" value="${escapeHTML(value)}" ${required ? "required" : ""} ${extra}></div>`;
    const selectField = (label, name, options, value = "", required = true) =>
        `<div class="field"><label for="${name}">${label}${required ? " *" : ""}</label><select id="${name}" name="${name}" ${required ? "required" : ""}><option value="">Selecciona una opción</option>${options.map(o => `<option value="${o.value}" ${String(o.value) === String(value) ? "selected" : ""}>${escapeHTML(o.label)}</option>`).join("")}</select></div>`;
    const formFooter = (submit = "Guardar") => `<div class="form-footer"><button type="button" class="btn btn-secondary" data-action="close-modal">Cancelar</button><button type="submit" class="btn btn-primary">${submit}</button></div>`;

    function openClientForm(id = null) {
        const item = id ? clientById(id) : { name: "", phone: "", email: "" };
        if (!item) return;
        modalOpen(id ? "Editar cliente" : "Registrar cliente", `<form id="clientForm"><div class="form-grid">
            ${field("Nombre completo", "name", item.name)}
            ${field("Teléfono", "phone", item.phone, "tel", true, 'placeholder="Ej. 300 123 4567"')}
            ${field("Correo electrónico", "email", item.email, "email", false, 'placeholder="cliente@correo.com"')}
            <div class="field full"><span class="form-help">Los campos marcados con * son obligatorios.</span></div>
        </div>${formFooter(id ? "Guardar cambios" : "Registrar cliente")}</form>`);
        $("#clientForm").addEventListener("submit", event => {
            event.preventDefault();
            const fd = new FormData(event.currentTarget);
            const name = String(fd.get("name")).trim(), phone = String(fd.get("phone")).trim(), email = String(fd.get("email")).trim();
            if (!name || !phone) return;
            const duplicate = data.clients.some(c => c.id !== Number(id) && c.phone.replace(/\s/g, "") === phone.replace(/\s/g, ""));
            if (duplicate) return showToast("Ya existe un cliente con ese teléfono.");
            if (id) Object.assign(item, { name, phone, email });
            else data.clients.push({ id: nextId(data.clients), name, phone, email });
            save(); modalClose(); render(); showToast(id ? "Cliente actualizado correctamente." : "Cliente registrado correctamente.");
        });
    }

    function openAppointmentForm(id = null) {
        const item = id ? data.appointments.find(a => a.id === Number(id)) : { clientId: "", barberId: "", serviceId: "", date: dateISO(new Date()), time: "09:00", status: "programada", notes: "" };
        if (!item) return;
        if (!data.clients.length || !data.barbers.length || !data.services.length) {
            showToast("Registra al menos un cliente, barbero y servicio antes de crear una cita."); return;
        }
        const clientOptions = data.clients.map(c => ({ value: c.id, label: c.name }));
        const barberOptions = data.barbers.map(b => ({ value: b.id, label: b.name }));
        const serviceOptions = data.services.map(s => ({ value: s.id, label: `${s.name} · ${money(s.price)}` }));
        modalOpen(id ? "Editar cita" : "Registrar nueva cita", `<form id="appointmentForm"><div class="form-grid">
            ${selectField("Cliente", "clientId", clientOptions, item.clientId)}
            ${selectField("Barbero", "barberId", barberOptions, item.barberId)}
            ${selectField("Servicio", "serviceId", serviceOptions, item.serviceId)}
            ${field("Fecha", "date", item.date, "date", true, `min="${dateISO(new Date())}"`)}
            ${field("Hora", "time", item.time, "time")}
            ${selectField("Estado", "status", [{value:"programada",label:"Programada"},{value:"realizada",label:"Realizada"},{value:"cancelada",label:"Cancelada"}], item.status)}
            <div class="field full"><label for="notes">Observaciones</label><textarea id="notes" name="notes" placeholder="Información adicional (opcional)">${escapeHTML(item.notes || "")}</textarea></div>
            <div class="field full"><span class="form-help">Se valida que el mismo barbero no tenga otra cita programada en la misma fecha y hora.</span></div>
        </div>${formFooter(id ? "Guardar cambios" : "Registrar cita")}</form>`);
        $("#appointmentForm").addEventListener("submit", event => {
            event.preventDefault();
            const fd = new FormData(event.currentTarget);
            const record = {
                clientId: Number(fd.get("clientId")), barberId: Number(fd.get("barberId")),
                serviceId: Number(fd.get("serviceId")), date: String(fd.get("date")),
                time: String(fd.get("time")), status: String(fd.get("status")), notes: String(fd.get("notes")).trim()
            };
            if (record.status === "programada") {
                const conflict = data.appointments.some(a => a.id !== Number(id) && a.barberId === record.barberId && a.date === record.date && a.time === record.time && a.status === "programada");
                if (conflict) return showToast("Ese barbero ya tiene una cita programada en esa fecha y hora.");
            }
            if (id) Object.assign(item, record);
            else data.appointments.push({ id: nextId(data.appointments), ...record });
            save(); modalClose(); render(); showToast(id ? "Cita actualizada correctamente." : "Cita registrada correctamente.");
        });
    }

    function openBarberForm(id = null) {
        const item = id ? barberById(id) : { name: "", specialty: "", phone: "" };
        if (!item) return;
        modalOpen(id ? "Editar barbero" : "Registrar barbero", `<form id="barberForm"><div class="form-grid">
            ${field("Nombre", "name", item.name)}
            ${field("Teléfono", "phone", item.phone, "tel", false)}
            <div class="field full"><label for="specialty">Especialidad</label><input id="specialty" name="specialty" value="${escapeHTML(item.specialty)}" placeholder="Ej. Corte clásico y moderno"></div>
        </div>${formFooter()}</form>`);
        $("#barberForm").addEventListener("submit", event => {
            event.preventDefault(); const fd = new FormData(event.currentTarget);
            const record = { name: String(fd.get("name")).trim(), phone: String(fd.get("phone")).trim(), specialty: String(fd.get("specialty")).trim() };
            if (!record.name) return;
            if (id) Object.assign(item, record); else data.barbers.push({ id: nextId(data.barbers), ...record });
            save(); modalClose(); render(); showToast(id ? "Barbero actualizado." : "Barbero registrado.");
        });
    }

    function openServiceForm(id = null) {
        const item = id ? serviceById(id) : { name: "", description: "", duration: 30, price: "" };
        if (!item) return;
        modalOpen(id ? "Editar servicio" : "Registrar servicio", `<form id="serviceForm"><div class="form-grid">
            ${field("Nombre del servicio", "name", item.name)}
            ${field("Precio (COP)", "price", item.price, "number", true, 'min="0" step="500"')}
            ${field("Duración (minutos)", "duration", item.duration, "number", true, 'min="5" step="5"')}
            <div class="field full"><label for="description">Descripción</label><textarea id="description" name="description" placeholder="Describe brevemente el servicio">${escapeHTML(item.description)}</textarea></div>
        </div>${formFooter()}</form>`);
        $("#serviceForm").addEventListener("submit", event => {
            event.preventDefault(); const fd = new FormData(event.currentTarget);
            const record = { name: String(fd.get("name")).trim(), price: Number(fd.get("price")), duration: Number(fd.get("duration")), description: String(fd.get("description")).trim() };
            if (!record.name || record.price < 0 || record.duration <= 0) return;
            if (id) Object.assign(item, record); else data.services.push({ id: nextId(data.services), ...record });
            save(); modalClose(); render(); showToast(id ? "Servicio actualizado." : "Servicio registrado.");
        });
    }

    const nextId = list => list.length ? Math.max(...list.map(item => Number(item.id) || 0)) + 1 : 1;
    function removeRecord(collection, id, label, renderView = currentView, dependencyCheck = null) {
        const item = collection.find(entry => entry.id === Number(id));
        if (!item) return;
        if (dependencyCheck && dependencyCheck(item)) return showToast(`No se puede eliminar: hay citas relacionadas con ${label.toLowerCase()}.`);
        if (!confirm(`¿Seguro que deseas eliminar ${label.toLowerCase()} "${item.name || item.id}"?`)) return;
        const index = collection.findIndex(entry => entry.id === Number(id));
        collection.splice(index, 1); save(); goTo(renderView); showToast(`${label} eliminado.`);
    }

    function handleAction(action, id) {
        switch (action) {
            case "new-client": openClientForm(); break;
            case "edit-client": openClientForm(id); break;
            case "delete-client": removeRecord(data.clients, id, "Cliente", "clientes", item => data.appointments.some(a => a.clientId === item.id)); break;
            case "new-appointment": openAppointmentForm(); break;
            case "edit-appointment": openAppointmentForm(id); break;
            case "delete-appointment": removeRecord(data.appointments, id, "Cita"); break;
            case "cancel-appointment": {
                const item = data.appointments.find(a => a.id === Number(id));
                if (item && confirm("¿Deseas cancelar esta cita?")) { item.status = "cancelada"; save(); render(); showToast("Cita cancelada."); }
                break;
            }
            case "complete-appointment": {
                const item = data.appointments.find(a => a.id === Number(id));
                if (item) { item.status = "realizada"; save(); render(); showToast("Cita marcada como realizada."); }
                break;
            }
            case "new-barber": openBarberForm(); break;
            case "edit-barber": openBarberForm(id); break;
            case "delete-barber": removeRecord(data.barbers, id, "Barbero", "barberos", item => data.appointments.some(a => a.barberId === item.id)); break;
            case "new-service": openServiceForm(); break;
            case "edit-service": openServiceForm(id); break;
            case "delete-service": removeRecord(data.services, id, "Servicio", "servicios", item => data.appointments.some(a => a.serviceId === item.id)); break;
            case "close-modal": modalClose(); break;
        }
    }

    document.addEventListener("click", event => {
        const nav = event.target.closest("[data-view]");
        if (nav) { event.preventDefault(); goTo(nav.dataset.view); return; }
        const navigate = event.target.closest("[data-navigate]");
        if (navigate) { goTo(navigate.dataset.navigate); return; }
        const actionElement = event.target.closest("[data-action]");
        if (actionElement) { event.preventDefault(); handleAction(actionElement.dataset.action, actionElement.dataset.id); }
    });
    $("#closeModal").addEventListener("click", modalClose);
    $("#modalBackdrop").addEventListener("click", event => { if (event.target === $("#modalBackdrop")) modalClose(); });
    document.addEventListener("keydown", event => { if (event.key === "Escape" && !$("#modalBackdrop").hidden) modalClose(); });
    $("#mobileMenu").addEventListener("click", () => {
        const open = $("#sidebar").classList.toggle("open");
        $("#mobileMenu").setAttribute("aria-expanded", String(open));
    });
    $("#todayLabel").textContent = today.toLocaleDateString("es-CO", { weekday: "short", day: "numeric", month: "short", year: "numeric" });
    const initialView = window.location.hash.replace("#", "");
    if (["inicio", "citas", "clientes", "barberos", "servicios", "historial"].includes(initialView)) currentView = initialView;
    render();
})();