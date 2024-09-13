document.addEventListener("DOMContentLoaded", function () {
    obtenerPacientes();
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
// Función para obtener los datos de un paciente
function obtenerPaciente(id) {
    return fetch("/pacientes/" + id)
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener el paciente");
            }
            return response.json();
        })
        .catch(error => {
            console.error("Hubo un problema con la petición fetch:", error);
        });
}

// Función para renderizar el listado de pacientes en la tabla
function renderizarPacientes(pacientes) {
    const pacientesList = document.getElementById("tbody-pacientes");
    pacientesList.innerHTML = "";

    pacientes.forEach(paciente => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <th scope="row">${paciente.id}</th>
            <td>${paciente.nombre}</td>
            <td>${paciente.apellido}</td>
            <td>${paciente.dni}</td>
            <td>${paciente.fechaAlta}</td>
            <td>${paciente.domicilio.calle}</td>
            <td>${paciente.domicilio.numero}</td>
            <td>${paciente.domicilio.localidad}</td>
            <td>${paciente.domicilio.provincia}</td>
            <td>
                <div style="display: flex">
                    <button data-paciente-id="${paciente.id}" data-tooltip="Editar" class="outline btn-editar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                             stroke-linejoin="round" class="lucide lucide-square-pen">
                            <path d="M12 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                            <path d="M18.375 2.625a1 1 0 0 1 3 3l-9.013 9.014a2 2 0 0 1-.853.505l-2.873.84a.5.5 0 0 1-.62-.62l.84-2.873a2 2 0 0 1 .506-.852z"/>
                        </svg>
                    </button>
                    <button data-paciente-id="${paciente.id}" data-tooltip="Borrar" class="outline contrast btn-borrar">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                             stroke-linejoin="round" class="lucide lucide-trash">
                            <path d="M3 6h18"/>
                            <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/>
                            <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/>
                        </svg>
                    </button>
                </div>
            </td>
        `;

        pacientesList.appendChild(fila);
    });
    funcionalidadBtnBorrar();
    funcionalidadBtnEditar();
}

// Enviar formulario de agregar paciente
document.getElementById("btn-guardar-form-agregar-paciente").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-agregar-paciente");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-agregar-paciente");
    const paciente = {
        nombre: form.elements["nombre"].value,
        apellido: form.elements["apellido"].value,
        dni: form.elements["dni"].value,
        domicilio: {
            calle: form.elements["domicilio"].value,
            numero: form.elements["numero"].value,
            localidad: form.elements["localidad"].value,
            provincia: form.elements["provincia"].value
        }
    };
    try {
        const response = await fetch("/pacientes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paciente)
        });
        const data = await response.json();
        obtenerPacientes();
        // Cierra el modal
        closeModalAnimation("dialog-agregar-paciente");
        mostrarToast("Paciente agregado correctamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/pacientes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paciente)
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
                obtenerPacientes();
                // Cierra el modal
                closeModalAnimation("dialog-agregar-paciente");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});
// Enviar formulario de borrar paciente
document.getElementById("btn-guardar-form-borrar-paciente").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-borrar-paciente");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-borrar-paciente");
    const pacienteId = form.elements["paciente-borrar-id-hidden"].value;
    try {
        const response = await fetch("/pacientes/" + pacienteId, {
            method: "DELETE"
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerPacientes();
        // Cierra el modal
        closeModalAnimation("dialog-borrar-paciente");
        mostrarToast("Paciente borrado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/pacientes/" + pacienteId, {
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
                obtenerPacientes();
                // Cierra el modal
                closeModalAnimation("dialog-borrar-paciente");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error.message);
            });*/
});
// Enviar formulario de editar paciente
document.getElementById("btn-guardar-form-editar-paciente").addEventListener("click", async function () {
    // Ponemos el botón en espera
    const btnGuardar = document.getElementById("btn-guardar-form-editar-paciente");
    btnGuardar.setAttribute("aria-busy", "true");
    const form = document.getElementById("form-editar-paciente");
    const paciente = {
        id: form.elements["paciente-editar-id-hidden"].value,
        nombre: form.elements["nombre-editar"].value,
        apellido: form.elements["apellido-editar"].value,
        dni: form.elements["dni-editar"].value,
        fechaAlta: form.elements["fecha-alta-editar"].value,
        domicilio: {
            calle: form.elements["domicilio-editar"].value,
            numero: form.elements["numero-editar"].value,
            localidad: form.elements["localidad-editar"].value,
            provincia: form.elements["provincia-editar"].value
        }
    };
    try {
        const response = await fetch("/pacientes/" + paciente.id, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paciente)
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }
        obtenerPacientes();
        // Cierra el modal
        closeModalAnimation("dialog-editar-paciente");
        mostrarToast("Paciente editado exitosamente", 0);
    } catch (error) {
        console.error("Hubo un problema con la petición fetch:", error.message);
        mostrarToast(error.message, 1);
    } finally {
        btnGuardar.removeAttribute("aria-busy");
    }
    /*fetch("/pacientes", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paciente)
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
                obtenerPacientes();
                // Cierra el modal
                closeModalAnimation("dialog-editar-paciente");
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

// Abrir el modal de agregar paciente
document.getElementById("btn-agregar-paciente").addEventListener("click", function () {
    openModalAnimation("dialog-agregar-paciente");
});
// Cerrar o cancelar el modal de agregar paciente
document.getElementById("btn-cancelar-form-agregar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-agregar-paciente");
});
document.getElementById("btn-close-form-agregar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-agregar-paciente");
});

// Abrir el modal de borrar paciente
function funcionalidadBtnBorrar() {
    const btnsBorrar = document.getElementsByClassName("btn-borrar");
    for (let btn of btnsBorrar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-borrar-paciente");
            // Asignar el id del paciente al abrir el modal
            const pacienteId = this.getAttribute("data-paciente-id");
            document.getElementById("paciente-borrar-id-hidden").value = pacienteId;
            // Cargar los datos del paciente
            obtenerPaciente(pacienteId).then(paciente => {
                document.getElementById("paciente-borrar-id").value = paciente.id;
                document.getElementById("paciente-borrar-nombre").value = paciente.nombre + " " + paciente.apellido;
            });
        });
    }
}
// Cerrar o cancelar el modal de borrar paciente
document.getElementById("btn-cancelar-form-borrar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-paciente");
});
document.getElementById("btn-close-form-borrar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-paciente");
});

// Abrir el modal de editar paciente
function funcionalidadBtnEditar() {
    const btnsEditar = document.getElementsByClassName("btn-editar");
    for (let btn of btnsEditar) {
        btn.addEventListener("click", function () {
            openModalAnimation("dialog-editar-paciente");
            // Asignar el id del paciente al abrir el modal
            const pacienteId = this.getAttribute("data-paciente-id");
            document.getElementById("paciente-editar-id-hidden").value = pacienteId;
            // Cargar los datos del paciente
            obtenerPaciente(pacienteId).then(paciente => {
                document.getElementById("nombre-editar").value = paciente.nombre;
                document.getElementById("apellido-editar").value = paciente.apellido;
                document.getElementById("dni-editar").value = paciente.dni;
                document.getElementById("fecha-alta-editar").value = paciente.fechaAlta;
                document.getElementById("domicilio-editar").value = paciente.domicilio.calle;
                document.getElementById("numero-editar").value = paciente.domicilio.numero;
                document.getElementById("localidad-editar").value = paciente.domicilio.localidad;
                document.getElementById("provincia-editar").value = paciente.domicilio.provincia;
            });
        });
    }
}
// Cerrar o cancelar el modal de editar paciente
document.getElementById("btn-cancelar-form-editar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-paciente");
});
document.getElementById("btn-close-form-editar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-editar-paciente");
});