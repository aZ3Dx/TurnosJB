document.addEventListener("DOMContentLoaded", function () {
    obtenerTodo();
});

function mostrarToast(texto, estilo) {
    const estilos = [
        {
            background: "green",
            color: "white",
        },
        {
            background: "red",
            color: "white",
        }
    ]
    Toastify({
        text: texto,
        duration: 2000,
        gravity: "top",
        position: "center",
        style: estilos[estilo],
    }).showToast();
}

// Función para obtener los pacientes
function obtenerPacientes() {
    fetch("/pacientes")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los pacientes");
            }
            return response.json();
        })
        .then(data => {
            renderizarPacientes(data);
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}
// Función para obtener los odontólogos
function obtenerOdontologos() {
    fetch("/odontologos")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los odontólogos");
            }
            return response.json();
        })
        .then(data => {
            renderizarOdontologos(data);
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}
// Función para obtener los turnos
function obtenerTurnos() {
    fetch("/turnos")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los turnos");
            }
            return response.json();
        })
        .then(data => {
            renderizarTurnos(data);
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}
// Función para obtener todo
function obtenerTodo() {
    obtenerPacientes();
    obtenerOdontologos();
    obtenerTurnos();
}
// Función para obtener los datos de un turno
function obtenerTurno(id) {
    return fetch("/turnos/" + id)
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener el turno");
            }
            return response.json();
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}

// Función para renderizar los pacientes
function renderizarPacientes(pacientes) {
    const pacientesSelect = document.getElementById("paciente");
    pacientesSelect.innerHTML = "";
    pacientesSelect.innerHTML = `
        <option selected disabled value="">-- Paciente --</option>
    `;
    pacientes.forEach(paciente => {
        const option = document.createElement("option");
        option.value = paciente.id;
        option.textContent = paciente.nombre + " " + paciente.apellido;
        pacientesSelect.appendChild(option);
    });
}
// Función para renderizar los odontólogos
function renderizarOdontologos(odontologos) {
    const odontologosSelect = document.getElementById("odontologo");
    odontologosSelect.innerHTML = "";
    odontologosSelect.innerHTML = `
        <option selected disabled value="">-- Odontólogo --</option>
    `;
    odontologos.forEach(odontologo => {
        const option = document.createElement("option");
        option.value = odontologo.id;
        option.textContent = odontologo.nombre + " " + odontologo.apellido;
        odontologosSelect.appendChild(option);
    });
}
// Función para renderizar los turnos
function renderizarTurnos(turnos) {
    const turnosList = document.getElementById("listado-turnos");
    turnosList.innerHTML = "";

    turnos.forEach(turno => {
        const article = document.createElement("article");

        article.innerHTML = `
            <header>
                <p><strong>Turno</strong></p>
            </header>
            <p><strong>Fecha:</strong> ${turno.fecha}</p>
            <p><strong>Hora:</strong> ${turno.hora}</p>
            <p><strong>Paciente:</strong> ${turno.paciente.nombre} ${turno.paciente.apellido}</p>
            <p><strong>Odontólogo:</strong> ${turno.odontologo.nombre} ${turno.odontologo.apellido}</p>
            <footer>
                <div role="group">
                    <button data-turno-id="${turno.id}" type="button" class="contrast btn-borrar" data-tooltip="Borrar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                             fill="none"
                             stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                             class="lucide lucide-trash">
                            <path d="M3 6h18"/>
                            <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/>
                            <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/>
                        </svg>
                    </button>
                    <button data-turno-id="${turno.id}" type="button" data-tooltip="Editar" class="btn-editar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                             fill="none"
                             stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                             class="lucide lucide-square-pen">
                            <path d="M12 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                            <path d="M18.375 2.625a1 1 0 0 1 3 3l-9.013 9.014a2 2 0 0 1-.853.505l-2.873.84a.5.5 0 0 1-.62-.62l.84-2.873a2 2 0 0 1 .506-.852z"/>
                        </svg>
                    </button>
                    <button data-turno-id="${turno.id}" type="button" class="secondary btn-ver" data-tooltip="Ver">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                             fill="none"
                             stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                             class="lucide lucide-view">
                            <path d="M21 17v2a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-2"/>
                            <path d="M21 7V5a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2"/>
                            <circle cx="12" cy="12" r="1"/>
                            <path d="M18.944 12.33a1 1 0 0 0 0-.66 7.5 7.5 0 0 0-13.888 0 1 1 0 0 0 0 .66 7.5 7.5 0 0 0 13.888 0"/>
                        </svg>
                    </button>
                </div>
            </footer>
        `;

        turnosList.appendChild(article);
    });
    funcionalidadBtnBorrar();
    funcionalidadBtnEditar();
}

// Enviar formulario de agregar turno
document.getElementById("btn-agregar-turno").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-agregar-turno");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-agregar-turno");
    const turno = {
        fecha: form.elements["fecha"].value,
        hora: form.elements["hora"].value,
        paciente: {
            id: form.elements["paciente"].value
        },
        odontologo: {
            id: form.elements["odontologo"].value
        }
    };
    try {
        const response = await fetch("/turnos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(turno)
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerTodo();
        mostrarToast("Turno agregado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/turnos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(turno)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
                return response.json();
            })
            .then(data => {
                obtenerTodo();
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});
// Enviar formulario de borrar turno
document.getElementById("btn-guardar-form-borrar-turno").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-borrar-turno");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-borrar-turno");
    const pacienteId = form.elements["turno-borrar-id-hidden"].value;
    try {
        const response = await fetch("/turnos/" + pacienteId, {
            method: "DELETE"
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerTodo();
        // Cierra el modal
        closeModalAnimation("dialog-borrar-turno");
        mostrarToast("Turno borrado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/turnos/" + pacienteId, {
            method: "DELETE"
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
            })
            .then(data => {
                obtenerTodo();
                // Cierra el modal
                closeModalAnimation("dialog-borrar-turno");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});
// Enviar formulario de editar turno
document.getElementById("btn-guardar-form-editar-turno").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-editar-turno");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-editar-turno");
    const turno = {
        id: form.elements["turno-editar-id-hidden"].value,
        fecha: form.elements["fecha-editar"].value,
        hora: form.elements["hora-editar"].value,
        paciente: {
            id: form.elements["paciente-editar-id-hidden"].value
        },
        odontologo: {
            id: form.elements["odontologo-editar-id-hidden"].value
        }
    };
    try {
        const response = await fetch("/turnos", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(turno)
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerTodo();
        // Cierra el modal
        closeModalAnimation("dialog-editar-turno");
        mostrarToast("Turno editado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/turnos", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(turno)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
            })
            .then(data => {
                obtenerTodo();
                // Cierra el modal
                closeModalAnimation("dialog-editar-turno");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});

// Agregar clases al HTML por 400ms
function openModalAnimation(id_modal) {
    document.getElementById(id_modal).setAttribute("open", "");
    document.documentElement.classList.add("modal-is-opening", "modal-is-open");
    setTimeout(() => document.documentElement.classList.remove("modal-is-opening"), 400);
}
function closeModalAnimation(id_modal) {
    document.documentElement.classList.add("modal-is-closing");
    setTimeout(() => {
        document.documentElement.classList.remove("modal-is-closing", "modal-is-open");
        document.getElementById(id_modal).removeAttribute("open");
    }, 400);
}

// Abrir el modal de borrar turno
function funcionalidadBtnBorrar() {
    const btnsBorrar = document.getElementsByClassName("btn-borrar");
    for (let btn of btnsBorrar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-borrar-turno");
            // Asignar el id del turno al abrir el modal
            const turnoId = this.getAttribute("data-turno-id");
            document.getElementById("turno-borrar-id-hidden").value = turnoId;
            // Cargar los datos del turno
            obtenerTurno(turnoId).then(turno => {
                document.getElementById("paciente-borrar").value = turno.paciente.nombre + " " + turno.paciente.apellido;
                document.getElementById("odontologo-borrar").value = turno.odontologo.nombre + " " + turno.odontologo.apellido;
                document.getElementById("fecha-borrar").value = turno.fecha;
                document.getElementById("hora-borrar").value = turno.hora;
            });
        });
    }
}
// Cerrar o cancelar el modal de borrar turno
document.getElementById("btn-cancelar-form-borrar-turno").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-turno");
});
document.getElementById("btn-close-form-borrar-turno").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-turno");
});

// Abrir el modal de editar turno
function funcionalidadBtnEditar() {
    const btnsEditar = document.getElementsByClassName("btn-editar");
    for (let btn of btnsEditar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-editar-turno");
            // Asignar el id del turno al abrir el modal
            const turnoId = this.getAttribute("data-turno-id");
            document.getElementById("turno-editar-id-hidden").value = turnoId;
            // Cargar los datos del turno
            obtenerTurno(turnoId).then(turno => {
                document.getElementById("paciente-editar-id-hidden").value = turno.paciente.id;
                document.getElementById("odontologo-editar-id-hidden").value = turno.odontologo.id;
                document.getElementById("paciente-editar").value = turno.paciente.nombre + " " + turno.paciente.apellido;
                document.getElementById("odontologo-editar").value = turno.odontologo.nombre + " " + turno.odontologo.apellido;
                document.getElementById("fecha-editar").value = turno.fecha;
                document.getElementById("hora-editar").value = turno.hora;
            })
        });
    }
}
// Cerrar o cancelar el modal de editar paciente
document.getElementById("btn-cancelar-form-editar-turno").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-turno");
});
document.getElementById("btn-close-form-editar-turno").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-turno");
});