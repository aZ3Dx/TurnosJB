document.addEventListener("DOMContentLoaded", function () {
    obtenerPacientes();
});

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
                    <button id="btn-editar" data-paciente-id="${paciente.id}" data-tooltip="Editar" class="outline">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                             stroke-linejoin="round" class="lucide lucide-square-pen">
                            <path d="M12 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                            <path d="M18.375 2.625a1 1 0 0 1 3 3l-9.013 9.014a2 2 0 0 1-.853.505l-2.873.84a.5.5 0 0 1-.62-.62l.84-2.873a2 2 0 0 1 .506-.852z"/>
                        </svg>
                    </button>
                    <button id="btn-borrar" data-paciente-id="${paciente.id}" data-tooltip="Borrar" class="outline contrast">
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
}

// Enviar formulario de agregar paciente
document.getElementById("btn-guardar-form-agregar-paciente").addEventListener("click", function () {
    const form = document.getElementById("form-agregar-paciente");
    const paciente = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        dni: document.getElementById("dni").value,
        domicilio: {
            calle: document.getElementById("domicilio").value,
            numero: document.getElementById("numero").value,
            localidad: document.getElementById("localidad").value,
            provincia: document.getElementById("provincia").value
        }
    };
    fetch("/pacientes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paciente)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Error al agregar el paciente");
                }
                return response.json();
            })
            .then(data => {
                obtenerPacientes();
                // Cierra el modal
                closeModalAnimation("dialog-agregar-paciente");
            })
            .catch(error => {
                console.error("Hubo un problema con la petición fetch:", error);
            });
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
document.getElementById("btn-borrar").addEventListener("click", function () {
    openModalAnimation("dialog-borrar-paciente");
})
// Cerrar o cancelar el modal de borrar paciente
document.getElementById("btn-cancelar-form-borrar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-paciente");
});
document.getElementById("btn-close-form-borrar-paciente").addEventListener("click", function () {
    closeModalAnimation("dialog-borrar-paciente");
});